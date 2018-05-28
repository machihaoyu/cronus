package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.Common.CommonRedisConst;
import com.fjs.cronus.controller.UserController;
import com.fjs.cronus.dto.AllocateForAvatarDTO;
import com.fjs.cronus.dto.cronus.*;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.thea.BaseChannelDTO;
import com.fjs.cronus.dto.thea.BaseCommonDTO;
import com.fjs.cronus.entity.CompanyMediaQueue;
import com.fjs.cronus.entity.UserMonthInfoDetail;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CompanyMediaQueueMapper;
import com.fjs.cronus.mappers.UserMonthInfoDetailMapper;
import com.fjs.cronus.mappers.UserMonthInfoMapper;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.UserMonthInfo;
import com.fjs.cronus.service.client.TheaService;
import com.fjs.cronus.service.redis.AllocateRedisService;
import com.fjs.cronus.service.redis.CRMRedisLockHelp;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import static java.util.stream.Collectors.*;

/**
 * Created by feng on 2017/9/22.
 */
@Service
public class UserMonthInfoService {

    private static final Logger logger = LoggerFactory.getLogger(UserMonthInfoService.class);

    @Autowired
    private AllocateLogService allocateLogService;

    @Autowired
    private UserMonthInfoMapper userMonthInfoMapper;

    @Autowired
    private CRMRedisLockHelp cRMRedisLockHelp;

    @Autowired
    private AllocateRedisService allocateRedisService;

    @Autowired
    private CompanyMediaQueueMapper companyMediaQueueMapper;

    @Autowired
    private UserMonthInfoDetailMapper userMonthInfoDetailMapper;

    @Autowired
    private CompanyMediaQueueService companyMediaQueueService;

    @Autowired
    private TheaService theaService;

    public List<UserMonthInfo> selectByParamsMap(Map<String, Object> map) {
        return userMonthInfoMapper.selectByParamsMap(map);
    }

    public Integer insertList(List<UserMonthInfo> userMonthInfoList) {
        // 这里使用的tk.mybatis的批量，包一层；
        // 原因：在无指定值的字段会插入null，如果mysql上设置了int(2) DEFAULT '1'不会为默认值
        userMonthInfoList.stream().forEach(i -> i.setStatus(CommonEnum.entity_status1.getCode()));
        return userMonthInfoMapper.insertList(userMonthInfoList);
    }

    public void copyCurrentMonthDataToNexMonth(Integer updateUserId, Integer companyid) {

        String key = CommonRedisConst.USERMONTHINFO_COPY.concat("$").concat(companyid.toString());
        Long lockToken = null;
        try {
            lockToken = cRMRedisLockHelp.lockBySetNX(key);

            Date now = new Date();
            Object o = null;
            // ====== 业务分析 ======
            // 1、拷贝是只将该一级吧，当月的分配数(月分配数、月奖励数)---拷贝到--->下月
            // 2、只拷贝用户关注的特殊渠道
            // 3、如果是，关注且存在--->覆盖、如果是关注且不存在--->新增、如果是不关注且存在--->归0

            // 获取当月、下月的effective_date
            String currentMothStr = allocateRedisService.getMonthStr(CommonConst.USER_MONTH_INFO_MONTH_CURRENT);
            String nextMothfStr = allocateRedisService.getMonthStr(CommonConst.USER_MONTH_INFO_MONTH_NEXT);

            // 获取用户关注的媒体
            Set<Integer> followMediaSet = this.companyMediaQueueService.findFollowMediaidAll(companyid);

            // 获取下月的分配数
            UserMonthInfo whereParams = new UserMonthInfo();
            whereParams.setStatus(CommonEnum.entity_status1.getCode());
            whereParams.setCompanyid(companyid);
            whereParams.setEffectiveDate(nextMothfStr);
            List<UserMonthInfo> nextMonthAllDataList = userMonthInfoMapper.select(whereParams);
            nextMonthAllDataList = CollectionUtils.isEmpty(nextMonthAllDataList) ? new ArrayList<>() : nextMonthAllDataList;

            Map<String, List<UserMonthInfo>> nextMonthData = nextMonthAllDataList.stream().collect(groupingBy(i -> {
                return i.getUserId() + "$" + i.getMediaid();
            }));

            // 获取当月数据、关注着的分配数
            Example example = new Example(UserMonthInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("status", CommonEnum.entity_status1.getCode());
            criteria.andEqualTo("companyid", companyid);
            criteria.andEqualTo("effectiveDate", currentMothStr);
            criteria.andIn("mediaid", followMediaSet);
            List<UserMonthInfo> currentMothDataList = userMonthInfoMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(currentMothDataList)) {
                return;
            }

            // 找要入库的数据
            List<UserMonthInfo> toCoverData = new ArrayList<>();    // 覆被盖
            Set<Integer> toInitData = nextMonthAllDataList.stream().filter(i -> i != null && !followMediaSet.contains(i.getMediaid())).map(UserMonthInfo::getId).collect(toSet()); // 要归0
            List<UserMonthInfo> toNewData = new ArrayList<>();      // 要新增的

            for (UserMonthInfo userMonthInfo : currentMothDataList) {
                String key2 = userMonthInfo.getUserId() + "$" + userMonthInfo.getMediaid();
                List<UserMonthInfo> list = nextMonthData.get(key2);
                if (CollectionUtils.isEmpty(list) || list.get(0) == null) {
                    // 当月有，下月无 ---> 新增
                    UserMonthInfo copy = new UserMonthInfo();
                    copy.setBaseCustomerNum(userMonthInfo.getBaseCustomerNum());
                    copy.setRewardCustomerNum(userMonthInfo.getRewardCustomerNum());
                    copy.setAssignedCustomerNum(0);
                    copy.setEffectiveCustomerNum(0);
                    copy.setEffectiveDate(nextMothfStr);
                    copy.setLastUpdateTime(now);
                    copy.setCreateTime(now);
                    copy.setUserId(userMonthInfo.getUserId());
                    copy.setCreateUserId(updateUserId);
                    copy.setLastUpdateUser(updateUserId);
                    copy.setCompanyid(companyid);
                    copy.setMediaid(userMonthInfo.getMediaid());
                    copy.setStatus(CommonEnum.entity_status1.getCode());
                    toNewData.add(copy);
                } else if (followMediaSet.contains(list.get(0).getMediaid())) {
                    // 当月有，下月有 ---> 覆被盖
                    UserMonthInfo copy = new UserMonthInfo();
                    copy.setBaseCustomerNum(userMonthInfo.getBaseCustomerNum());
                    copy.setRewardCustomerNum(userMonthInfo.getRewardCustomerNum());
                    copy.setAssignedCustomerNum(0);
                    copy.setEffectiveCustomerNum(0);
                    copy.setLastUpdateTime(now);
                    copy.setLastUpdateUser(updateUserId);
                    copy.setId(list.get(0).getId());
                    toCoverData.add(copy);
                }
            }

            // 入库
            if (CollectionUtils.isNotEmpty(toInitData)) {
                UserMonthInfo value = new UserMonthInfo();
                value.setBaseCustomerNum(0);
                value.setRewardCustomerNum(0);
                value.setAssignedCustomerNum(0);
                value.setEffectiveCustomerNum(0);
                value.setLastUpdateTime(now);
                value.setLastUpdateUser(updateUserId);

                Example ee = new Example(UserMonthInfo.class);
                Example.Criteria criteria1 = ee.createCriteria();
                criteria1.andIn("id", toInitData);
                criteria1.andEqualTo("status", CommonEnum.entity_status1.getCode());
                userMonthInfoMapper.updateByExampleSelective(value, ee);
            }

            for (UserMonthInfo s : toCoverData) {
                UserMonthInfo value = new UserMonthInfo();
                value.setBaseCustomerNum(s.getBaseCustomerNum());
                value.setRewardCustomerNum(s.getRewardCustomerNum());
                value.setAssignedCustomerNum(0);
                value.setEffectiveCustomerNum(0);
                value.setLastUpdateTime(now);
                value.setLastUpdateUser(updateUserId);


                Example ee = new Example(UserMonthInfo.class);
                Example.Criteria criteria1 = ee.createCriteria();
                criteria1.andEqualTo("status", CommonEnum.entity_status1.getCode());
                criteria1.andEqualTo("id", s.getId());

                userMonthInfoMapper.updateByExampleSelective(value, ee);
            }

            if (CollectionUtils.isNotEmpty(toNewData)) {
                this.insertList(toNewData);
            }

            if (CollectionUtils.isNotEmpty(followMediaSet)) {
                // 拷贝redis队列
                for (Integer mediaId : followMediaSet) {
                    allocateRedisService.copyCurrentMonthQueue(companyid, mediaId);
                }
            }

            // 在20秒内未完成运算，视为失败；事务回滚；redis解锁
            long l = cRMRedisLockHelp.getCurrentTimeFromRedisServicer() - lockToken;
            if (l > 20 * 1000) {
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, "服务超时");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this.cRMRedisLockHelp.unlockForSetNx2(key, lockToken);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void editUserMonthInfo(Integer loginUserId, Integer userId, Integer companyid, Integer mediaid, String monthFlag, Integer baseCustomerNum, Integer rewardCustomerNum) {


        // 校验
        Set<Integer> mediaidAll = this.companyMediaQueueService.findFollowMediaidAll(companyid);
        if (!mediaidAll.contains(mediaid)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "参数错误，该一级吧未关注此媒体（id=" + mediaid + "）");
        }


        // 锁
        String key = CommonRedisConst.USERMONTHINFO_EDIT.concat("$").concat(companyid.toString()).concat("$").concat(mediaid.toString());

        Long lockToken = null;
        try {
            lockToken = cRMRedisLockHelp.lockBySetNX(key);
            // ===== 业务校验 =====
            // 总分配队列新增不用校验
            // 减少情况
            // 1、总分配队列的[月申请数]、[月奖励数]  > 其他特殊渠道[月申请数]之和、[月奖励数]之和
            // 2、特殊渠道 [月申请数]、[月奖励数] 不能分别 >  (总分配队列[月申请数]、[月奖励数] 分别 - 剩余特殊渠道[月申请数]之和、[月奖励数]之和)

            String effectiveDate = this.allocateRedisService.getMonthStr(monthFlag);

            // 获取用户所以媒体、具体月份、具体吧的分配数据
            UserMonthInfo ee = new UserMonthInfo();
            ee.setCompanyid(companyid);
            ee.setUserId(userId);
            ee.setEffectiveDate(effectiveDate);
            ee.setStatus(CommonEnum.entity_status1.getCode());
            List<UserMonthInfo> userAllMedialDataList = userMonthInfoMapper.select(ee);
            userAllMedialDataList = CollectionUtils.isEmpty(userAllMedialDataList) ? new ArrayList<>() : userAllMedialDataList;

            if (CommonConst.COMPANY_MEDIA_QUEUE_COUNT.equals(mediaid)) {
                // 总分配队列情况

                List<UserMonthInfo> tempList = userAllMedialDataList.stream()
                        .filter(i ->
                                i != null
                                        && !CommonConst.COMPANY_MEDIA_QUEUE_COUNT.equals(i.getMediaid())
                                        && i.getBaseCustomerNum() != null
                                        && i.getRewardCustomerNum() != null
                        )
                        .collect(toList());

                Integer baseCustomerNumDB = tempList.stream().mapToInt(UserMonthInfo::getBaseCustomerNum).sum();
                Integer rewardCustomerNumDB = tempList.stream().mapToInt(UserMonthInfo::getRewardCustomerNum).sum();

                if (rewardCustomerNum < rewardCustomerNumDB) {
                    throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "月奖励数 不能小于 其他特殊渠道月奖励数之和");
                }
                if (baseCustomerNum < baseCustomerNumDB) {
                    throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "月分配数 不能小于 其他特殊渠道月分配数之和");
                }

            } else {
                // 特殊分配队列情况

                Integer rewardCustomerNumSum = 0;
                Integer baseCustomerNumSum = 0;
                UserMonthInfo countMedia = null; // 当月总队列的数据
                for (UserMonthInfo userMonthInfoTemp : userAllMedialDataList) {
                    if (CommonConst.COMPANY_MEDIA_QUEUE_COUNT.equals(userMonthInfoTemp.getMediaid())) {
                        countMedia = userMonthInfoTemp;
                    } else if (mediaid.equals(userMonthInfoTemp.getMediaid())) {
                        // 自队列数据的不做处理
                    } else { // 其他队列
                        // 获取除去总分配队列、当前分配队列外的特殊渠道 rewardCustomerNum 的和
                        rewardCustomerNumSum += userMonthInfoTemp.getRewardCustomerNum();
                        // 获取除去总分配队列、当前分配队列外的特殊渠道 baseCustomerNumSum 的和
                        baseCustomerNumSum += userMonthInfoTemp.getBaseCustomerNum();
                    }
                }

                if (countMedia == null) {
                    throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "无总队列数据，编辑特殊队列需要总队列数据");
                }
                if (rewardCustomerNum > (countMedia.getRewardCustomerNum() - rewardCustomerNumSum)) {
                    throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "月奖励数 不能大于 总分配队列-其他特殊渠道月奖励数后剩余的数");
                }
                if (baseCustomerNum > (countMedia.getBaseCustomerNum() - baseCustomerNumSum)) {
                    throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "月分配数 不能大于 总分配队列-其他特殊渠道月分配数后剩余的数");
                }
            }

            // ===== 业务校验 =====
            // 当月申请数需要 > 已分配数
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            Date targetDate = null;
            Date nowDate = null;
            try {
                targetDate = sdf.parse(effectiveDate);
                nowDate = sdf.parse(sdf.format(new Date()));
            } catch (ParseException e) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "effectiveDate 时间转换错误");
            }

            if (targetDate.compareTo(nowDate) <= 0) {
                // 当月需要校验，下月无需校验

                UserMonthInfo userMonthInfo = userAllMedialDataList.stream().filter(i -> i != null && mediaid.equals(i.getMediaid())).findAny().orElse(null);
                if (userMonthInfo != null && userMonthInfo.getAssignedCustomerNum() > (baseCustomerNum + rewardCustomerNum)) {
                    throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "月申请数和月奖励数之和需大于已分配数");
                }
            }

            // 数据入库
            UserMonthInfo valueParams = new UserMonthInfo();
            valueParams.setLastUpdateUser(loginUserId);
            valueParams.setBaseCustomerNum(baseCustomerNum);
            valueParams.setRewardCustomerNum(rewardCustomerNum);
            valueParams.setLastUpdateTime(new Date());

            Example example2 = new Example(UserMonthInfo.class);
            Example.Criteria criteria1 = example2.createCriteria();
            criteria1.andEqualTo("userId", userId);
            criteria1.andEqualTo("effectiveDate", effectiveDate);
            criteria1.andEqualTo("mediaid", mediaid);
            criteria1.andEqualTo("companyid", companyid);
            criteria1.andEqualTo("status", CommonEnum.entity_status1.getCode());

            userMonthInfoMapper.updateByExampleSelective(valueParams, example2);

            // 在20秒内未完成运算，视为失败；事务回滚；redis解锁
            long l = cRMRedisLockHelp.getCurrentTimeFromRedisServicer() - lockToken;
            if (l > 20 * 1000) {
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, "服务超时");
            }
        } catch (Exception e) {
            // 直接往controller抛，让其处理
            throw e;
        } finally {
            // redis解锁
            this.cRMRedisLockHelp.unlockForSetNx2(key, lockToken);
        }
    }

    /**
     * ocdc 推送后，自动分配成功后，记录该业务员的分配数.
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void incrNum2DBForOCDCPush(AllocateForAvatarDTO signCustomAllocate, Integer mediaId, String currentMonth, CustomerDTO customerDTO) {
        // ------ 业务分析 ------
        // 先找媒体队列的分配数据，无，说明未设置，说明一级巴长只设置了总队列分配数
        //

        // 查询并启用悲观锁
        // 应该至少找到一条记录，最多2条（总分配队列、当前媒体的队列）
        List<UserMonthInfo> mediaDataList = userMonthInfoMapper.findByParamsForUpdate(signCustomAllocate.getCompanyid(), mediaId, CommonConst.COMPANY_MEDIA_QUEUE_COUNT, signCustomAllocate.getSalesmanId(), currentMonth, CommonEnum.entity_status1.getCode());

        Integer id = null;
        Date now = new Date();

        // 主表和特殊队列表都++
        if (CollectionUtils.isEmpty(mediaDataList)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "数据异常，分配给了该员工，但未找到分配数据（包括总队列也未找到）（subCompanyId=" + signCustomAllocate.getCompanyid() + "，Mediaid=" + mediaId + "，salesmanId=" + signCustomAllocate.getSalesmanId() + "，currentMonth=" + currentMonth + "）");
        } else {

            Map<Integer, List<UserMonthInfo>> mediaIdMappingData = mediaDataList.stream().collect(groupingBy(UserMonthInfo::getMediaid));
            if (mediaIdMappingData.size() == 0 || mediaIdMappingData.size() > 2) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "数据异常，应该最多2条（subCompanyId=" + signCustomAllocate.getCompanyid() + "，Mediaid=" + mediaId + "，salesmanId=" + signCustomAllocate.getSalesmanId() + "，currentMonth=" + currentMonth + "）");
            }

            List<UserMonthInfo> list = mediaIdMappingData.get(CommonConst.COMPANY_MEDIA_QUEUE_COUNT);
            UserMonthInfo countData = list.get(0);// 总分配队列

            List<UserMonthInfo> list2 = mediaIdMappingData.get(mediaId);
            UserMonthInfo mediaData = list2 == null ? null : list2.get(0);// 特殊分配队列

            if (mediaData == null) {
                // 特殊分配队列没有时，需要手动创建

                // 新建
                UserMonthInfo userMonthInfoTemp = new UserMonthInfo();
                userMonthInfoTemp.setBaseCustomerNum(1);
                userMonthInfoTemp.setRewardCustomerNum(0);
                userMonthInfoTemp.setAssignedCustomerNum(1);
                userMonthInfoTemp.setEffectiveCustomerNum(0);
                userMonthInfoTemp.setEffectiveDate(currentMonth);
                userMonthInfoTemp.setLastUpdateTime(now);
                userMonthInfoTemp.setCreateTime(now);
                userMonthInfoTemp.setUserId(signCustomAllocate.getSalesmanId());
                //userMonthInfoTemp.setCreateUserId(userIdByOption);
                //userMonthInfoTemp.setLastUpdateUser(userIdByOption);
                userMonthInfoTemp.setCompanyid(signCustomAllocate.getCompanyid());
                userMonthInfoTemp.setMediaid(mediaId);
                userMonthInfoTemp.setStatus(CommonEnum.entity_status1.getCode());
                userMonthInfoMapper.insertUseGeneratedKeys(userMonthInfoTemp);

                id = userMonthInfoTemp.getId();

                Set<Integer> ids = new HashSet<>();
                ids.add(list.get(0).getId());// 只给总的加，特定媒体上面已经加过了
                userMonthInfoMapper.update2IncrNumForAssignedCustomerNum(ids, currentMonth);
            } else {

                id = mediaData.getId();

                Set<Integer> ids = new HashSet<>();
                ids.add(id);
                ids.add(countData.getId());
                userMonthInfoMapper.update2IncrNumForAssignedCustomerNum(ids, currentMonth);
            }

        }

        UserMonthInfoDetail detail = new UserMonthInfoDetail();
        detail.setCreated(now);
        detail.setUserId(signCustomAllocate.getSalesmanId());
        detail.setCompanyid(signCustomAllocate.getCompanyid());
        detail.setMediaid(mediaId);
        detail.setEffectiveDate(currentMonth);
        detail.setCustomerInfo(JSONObject.toJSONString(customerDTO));
        detail.setType(CommonConst.USER_MONTH_INFO_DETAIL_TYPE1);
        detail.setUserMonthInfoId(id);
        detail.setFromediaid(signCustomAllocate.getFrommediaid());
        detail.setCustomerid(customerDTO.getId());
        userMonthInfoDetailMapper.insertSelective(detail);
    }

    /**
     * 记录有效数.
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void incrNum2DB(CustomerInfo customerDto, Integer salesmanId, String token) {

        Integer subCompanyId = customerDto.getSubCompanyId();
        String utmSource = customerDto.getUtmSource();
        String currentMonthStr = this.allocateRedisService.getMonthStr(CommonConst.USER_MONTH_INFO_MONTH_CURRENT);

        Integer mediaid = this.getChannelInfoByChannelName(token, utmSource);
        Date now = new Date();

        // 先查该记录
        UserMonthInfo e = new UserMonthInfo();
        e.setCompanyid(subCompanyId);
        e.setMediaid(mediaid);
        e.setUserId(salesmanId);
        e.setEffectiveDate(currentMonthStr);
        e.setStatus(CommonEnum.entity_status1.getCode());
        List<UserMonthInfo> select = userMonthInfoMapper.findByParamsForUpdate(subCompanyId, mediaid, CommonConst.COMPANY_MEDIA_QUEUE_COUNT, salesmanId, currentMonthStr, CommonEnum.entity_status1.getCode());

        // 业务说明：一个用户只能算一次.
        UserMonthInfoDetail ee = new UserMonthInfoDetail();
        ee.setCustomerid(customerDto.getId());
        ee.setStatus(CommonEnum.entity_status1.getCode());
        ee.setType(CommonConst.USER_MONTH_INFO_DETAIL_TYPE2);
        List<UserMonthInfoDetail> dataExist = userMonthInfoDetailMapper.select(ee);
        if (CollectionUtils.isNotEmpty(dataExist)) {
            return;
        }

        Integer id = null;
        if (CollectionUtils.isEmpty(select) || select.size() != 2) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "数据异常，（subCompanyId=" + subCompanyId + "，Mediaid=" + mediaid + "，salesmanId=" + salesmanId + "，currentMonth=" + currentMonthStr + "）");
        }

        // 主表 incr
        Set<Integer> ids = select.stream().map(UserMonthInfo::getId).collect(toSet());
        userMonthInfoMapper.update2IncrNumForEffectiveCustomerNum(ids);
        id = select.stream().filter(i -> i != null && !CommonConst.COMPANY_MEDIA_QUEUE_COUNT.equals(i.getMediaid())).map(UserMonthInfo::getId).findAny().orElse(null);

        // 明细表记录明细
        UserMonthInfoDetail detail = new UserMonthInfoDetail();
        detail.setCreated(now);
        detail.setUserId(salesmanId);
        detail.setCompanyid(subCompanyId);
        detail.setMediaid(mediaid);
        detail.setEffectiveDate(currentMonthStr);
        detail.setCustomerInfo(JSONObject.toJSONString(customerDto));
        detail.setType(CommonConst.USER_MONTH_INFO_DETAIL_TYPE2);
        detail.setUserMonthInfoId(id);
        detail.setCustomerid(customerDto.getId());
        userMonthInfoDetailMapper.insertSelective(detail);
    }

    /**
     * 根据渠道获取渠道基本信息（目的获取来源id、媒体id）.
     */
    public Integer getChannelInfoByChannelName(String token, String UtmSource) {
        if (StringUtils.isBlank(UtmSource)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "UtmSource 不能为null");
        }
        JSONObject params = new JSONObject();
        params.put("channelName", UtmSource);
        TheaApiDTO<Integer> infoByChannelName = theaService.getMediaidByChannelName(token, params);


        if (infoByChannelName == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "请求thea服务，响应 infoByChannelName==null");
        }
        if (infoByChannelName.getResult() != 0) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "请求thea服务，响应 result !=0, mesage=" + infoByChannelName.getMessage());
        }

        if (infoByChannelName.getData() == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "请求thea服务，响应 data=null");
        }
        return infoByChannelName.getData();
    }

    /**
     * 查某一级吧下某媒体的已分配数.
     */
    public FindMediaAssignedCustomerNumDTO findAssignedCustomerNum(FindMediaAssignedCustomerNumDTO params) {

        List<FindMediaAssignedCustomerNumItmDTO> list = params.getList();
        for (FindMediaAssignedCustomerNumItmDTO item : list) {
            Integer selectSum = userMonthInfoMapper.selectSum(item.getCompanyid(), item.getMediaid(), item.getMonth(), CommonEnum.entity_status1.getCode());
            Integer sum = selectSum;
            item.setAssigned_customer_num(sum == null ? 0 : sum);
        }
        return params;
    }

    /**
     * 查某一级吧的已分配数.
     */
    public FindCompanyAssignedCustomerNumDTO findAssignedCustomerNum(FindCompanyAssignedCustomerNumDTO params) {

        List<FindCompanyAssignedCustomerNumItmDTO> list = params.getList();
        for (FindCompanyAssignedCustomerNumItmDTO item : list) {
            Integer selectSum = userMonthInfoMapper.selectSum(item.getCompanyid(), null, item.getMonth(), CommonEnum.entity_status1.getCode());
            Integer sum = selectSum;
            item.setAssigned_customer_num(sum == null ? 0 : sum);
        }
        return params;
    }

    /**
     * 总分配队列获取一级吧各媒体（除去总分配队列）月分配数详情.
     */
    public List<Map<String, Object>> findMonthAllocateData(String monthFlag, Integer companyid, Integer mediaid, Integer salemanid, String token) {
        logger.info("--- findMonthAllocateData ----> 1");

        String monthStr = allocateRedisService.getMonthStr(monthFlag);
        List<Map<String, Object>> result = new ArrayList<>();

        if (!CommonConst.COMPANY_MEDIA_QUEUE_COUNT.equals(mediaid)) {
            return result;
        }

        // 获取该业务分配情况
        UserMonthInfo e = new UserMonthInfo();
        e.setCompanyid(companyid);
        e.setEffectiveDate(monthStr);
        e.setUserId(salemanid);
        e.setStatus(CommonEnum.entity_status1.getCode());
        List<UserMonthInfo> select = userMonthInfoMapper.select(e);
        if (CollectionUtils.isEmpty(select)) {
            return result;
        }
        logger.info("--- findMonthAllocateData ----> 2");
        for (UserMonthInfo userMonthInfo : select) {
            logger.info("--- findMonthAllocateData ----> 3" + userMonthInfo.getId());
        }

        // 过滤掉总分配队列
        List<UserMonthInfo> collect = select.stream()
                .filter(i -> i != null
                        && i.getMediaid() != null
                        && i.getBaseCustomerNum() != null
                        && i.getBaseCustomerNum() > 0
                        && i.getRewardCustomerNum() != null
                        && i.getRewardCustomerNum() > 0
                        && !CommonConst.COMPANY_MEDIA_QUEUE_COUNT.equals(i.getMediaid())
                )
                .collect(toList());
        if (CollectionUtils.isEmpty(collect)) {
            return result;
        }
        logger.info("--- findMonthAllocateData ----> 4");
        for (UserMonthInfo userMonthInfo : collect) {
            logger.info("--- findMonthAllocateData ----> 5" + userMonthInfo.getId());
        }

        // 获取系统所有媒体
        TheaApiDTO<List<BaseCommonDTO>> allMedia = theaService.getAllMedia(token);
        if (!CommonMessage.SUCCESS.getCode().equals(allMedia.getResult())) {
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, allMedia.getMessage());
        }
        List<BaseCommonDTO> allMediaList = allMedia.getData();
        if (CollectionUtils.isEmpty(allMediaList)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "系统数据异常，未找到媒体数据");
        }
        Map<Integer, String> idMappingName = allMediaList.stream().collect(toMap(BaseCommonDTO::getId, BaseCommonDTO::getName, (x, y) -> x));
        if (idMappingName == null || idMappingName.size() == 0) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "系统数据异常，未找到媒体数据");
        }

        for (UserMonthInfo userMonthInfo : collect) {
            Map<String, Object> temp = new HashMap<>(1);
            temp.put("id", userMonthInfo.getId());
            temp.put("mediaid", userMonthInfo.getMediaid());
            temp.put("name", idMappingName.get(userMonthInfo.getMediaid()));
            temp.put("baseCustomerNum", userMonthInfo.getBaseCustomerNum());
            temp.put("rewardCustomerNum", userMonthInfo.getRewardCustomerNum());
            result.add(temp);
            logger.info("--- findMonthAllocateData ----> 6" + temp);
        }
        logger.info("--- findMonthAllocateData ----> 7" + result);

        return result;
    }

    /**
     * 获取一级吧媒体已分配数详情.
     */
    public List<Map<String, Object>> findAllocateData(String monthFlag, Integer companyid, Integer mediaid, Integer salemanid, String token) {

        String monthStr = allocateRedisService.getMonthStr(monthFlag);
        List<Map<String, Object>> result = new ArrayList<>();

        if (CommonConst.COMPANY_MEDIA_QUEUE_COUNT.equals(mediaid)) {
            // 总分配队列

            // 获取该业务分配情况
            Example example = new Example(UserMonthInfoDetail.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("status", CommonEnum.entity_status1.getCode());
            criteria.andEqualTo("companyid", companyid);
            criteria.andEqualTo("userId", salemanid);
            criteria.andEqualTo("effectiveDate", monthStr);
            criteria.andNotEqualTo("mediaid", CommonConst.COMPANY_MEDIA_QUEUE_COUNT);

            List<UserMonthInfoDetail> select = userMonthInfoDetailMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(select)) return result;

            Map<Integer, Long> collect = select.stream()
                    .filter(i -> i != null && i.getMediaid() != null)
                    .collect(groupingBy(UserMonthInfoDetail::getMediaid, counting()));
            if (collect == null || collect.size() == 0) {
                return result;
            }

            // 获取系统所有媒体
            TheaApiDTO<List<BaseCommonDTO>> allMedia = theaService.getAllMedia(token);
            if (!CommonMessage.SUCCESS.getCode().equals(allMedia.getResult())) {
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, allMedia.getMessage());
            }
            List<BaseCommonDTO> allMediaList = allMedia.getData();
            if (CollectionUtils.isEmpty(allMediaList)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "系统数据异常，未找到媒体数据");
            }
            Map<Integer, String> idMappingName = allMediaList.stream().collect(toMap(BaseCommonDTO::getId, BaseCommonDTO::getName, (x, y) -> x));
            if (idMappingName == null || idMappingName.size() == 0) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "系统数据异常，未找到媒体数据");
            }

            for (Map.Entry<Integer, Long> entry : collect.entrySet()) {
                Map<String, Object> temp = new HashMap<>();
                temp.put("mediaid", entry.getKey());
                temp.put("name", idMappingName.get(entry.getKey()));
                temp.put("assignedCustomerNum", entry.getValue());
                result.add(temp);
            }
            return result;
        } else {
            // 特殊分配队列

            // 获取系统所有媒体
            TheaApiDTO<List<BaseCommonDTO>> allMedia = theaService.getAllMedia(token);
            if (!CommonMessage.SUCCESS.getCode().equals(allMedia.getResult())) {
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, allMedia.getMessage());
            }
            List<BaseCommonDTO> allMediaList = allMedia.getData();
            if (CollectionUtils.isEmpty(allMediaList)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "系统数据异常，未找到媒体数据");
            }
            Map<Integer, String> idMappingName = allMediaList.stream().collect(toMap(BaseCommonDTO::getId, BaseCommonDTO::getName, (x, y) -> x));
            if (idMappingName == null || idMappingName.size() == 0) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "系统数据异常，未找到媒体数据");
            }

            // 获取该业务分配情况
            Set<Integer> mediaids = new HashSet<>();
            mediaids.add(mediaid);
            mediaids.add(CommonConst.COMPANY_MEDIA_QUEUE_COUNT);

            Example example = new Example(UserMonthInfoDetail.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("status", CommonEnum.entity_status1.getCode());
            criteria.andEqualTo("companyid", companyid);
            criteria.andEqualTo("userId", salemanid);
            criteria.andEqualTo("effectiveDate", monthStr);
            criteria.andIn("mediaid", mediaids);

            List<UserMonthInfoDetail> select = userMonthInfoDetailMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(select)) return result;
            Map<Integer, Long> collect = select.stream()
                    .filter(i -> i != null && i.getMediaid() != null)
                    .collect(groupingBy(UserMonthInfoDetail::getMediaid, counting()));
            if (collect == null || collect.size() == 0) {

                // 设置初始化值
                Map<String, Object> temp = new HashMap<>();
                temp.put("mediaid", mediaid);
                temp.put("name", idMappingName.get(mediaid));
                temp.put("assignedCustomerNum", 0);
                result.add(temp);

                temp = new HashMap<>();
                temp.put("mediaid", CommonConst.COMPANY_MEDIA_QUEUE_COUNT);
                temp.put("name", "总分配队列");
                temp.put("assignedCustomerNum", 0);
                result.add(temp);

                return result;
            } else {
                // 有数据
                result = new ArrayList<>();
                boolean b = false;
                for (Map.Entry<Integer, Long> entry : collect.entrySet()) {

                    b = CommonConst.COMPANY_MEDIA_QUEUE_COUNT.equals(entry.getKey()) ? true : false;
                    Map<String, Object> temp2 = new HashMap<>();
                    temp2.put("mediaid", entry.getKey());
                    temp2.put("name", b ? "总分配队列" : idMappingName.get(entry.getKey()));
                    temp2.put("assignedCustomerNum", entry.getValue());
                    result.add(temp2);
                }
                if (!b) {
                    Map<String, Object> temp = new HashMap<>();
                    temp.put("mediaid", CommonConst.COMPANY_MEDIA_QUEUE_COUNT);
                    temp.put("name", "总分配队列");
                    temp.put("assignedCustomerNum", 0);
                    result.add(temp);
                }
                return result;
            }
        }
    }
}
