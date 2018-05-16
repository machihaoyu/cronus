package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.Common.CommonRedisConst;
import com.fjs.cronus.dto.cronus.*;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.thea.BaseChannelDTO;
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
    private TheaService theaService;

    public List<UserMonthInfo> selectByParamsMap(Map<String, Object> map) {
        return userMonthInfoMapper.selectByParamsMap(map);
    }

    public Integer insertList(List<UserMonthInfo> userMonthInfoList) {
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
            CompanyMediaQueue e = new CompanyMediaQueue();
            e.setCompanyid(companyid);
            e.setStatus(CommonEnum.entity_status1.getCode());
            List<CompanyMediaQueue> companyMediaQueueList = companyMediaQueueMapper.select(e);
            Set<Integer> followMediaSet = CollectionUtils.isEmpty(companyMediaQueueList) ? new HashSet<>() : companyMediaQueueList.stream().map(CompanyMediaQueue::getMediaid).collect(toSet());
            if (CollectionUtils.isEmpty(followMediaSet)) {
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, "数据异常，未发现用户关注媒体队列数据");
            }

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
            Set<Integer> mediaSet = followMediaSet;
            mediaSet.add(CommonConst.COMPANY_MEDIA_QUEUE_COUNT);
            criteria.andIn("mediaid", mediaSet);
            List<UserMonthInfo> currentMothDataList = userMonthInfoMapper.selectByExample(example);
            currentMothDataList = CollectionUtils.isEmpty(currentMothDataList) ? new ArrayList<>() : currentMothDataList;

            // 找要入库的数据
            List<UserMonthInfo> toCoverData = new ArrayList<>();    // 覆被盖
            Set<Integer> toInitData = nextMonthAllDataList.stream().filter(i -> i != null && !mediaSet.contains(i.getMediaid())).map(UserMonthInfo::getId).collect(toSet()); // 要归0
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
                    toNewData.add(copy);
                } else if (mediaSet.contains(list.get(0).getMediaid())) {
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
                userMonthInfoMapper.updateByPrimaryKeySelective(s);
            }
            if (CollectionUtils.isNotEmpty(toNewData)) {
                userMonthInfoMapper.insertList(toNewData);
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
    public void incrNum2DB(Integer subCompanyId, BaseChannelDTO baseChannelDTO, Integer salesmanId, String currentMonth, CustomerDTO customerDTO) {

        UserMonthInfo e = new UserMonthInfo();
        e.setCompanyid(subCompanyId);
        e.setMediaid(baseChannelDTO.getMedia_id());
        e.setUserId(salesmanId);
        e.setEffectiveDate(currentMonth);
        e.setStatus(CommonEnum.entity_status1.getCode());
        List<UserMonthInfo> select = userMonthInfoMapper.findByParamsForUpdate(subCompanyId, baseChannelDTO.getMedia_id(), salesmanId, currentMonth, CommonEnum.entity_status1.getCode());

        Integer id = null;
        Date now = new Date();

        // 主表 incr
        if (CollectionUtils.isEmpty(select) || select.get(0) == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "数据异常，分配给了该员工，但未找到分配数据（subCompanyId=" + subCompanyId + "，Mediaid=" + baseChannelDTO.getMedia_id() + "，salesmanId=" + salesmanId + "，currentMonth=" + currentMonth + "）");
        } else {
            UserMonthInfo userMonthInfo = select.get(0);
            userMonthInfo.setSourceid(baseChannelDTO.getSource_id());
            userMonthInfo.setAccountid(baseChannelDTO.getAccount_id());
            userMonthInfo.setChannelid(baseChannelDTO.getId());
            userMonthInfoMapper.update2IncrNumForAssignedCustomerNum(userMonthInfo.getId());
            id = userMonthInfo.getId();
        }

        UserMonthInfoDetail detail = new UserMonthInfoDetail();
        detail.setCreated(now);
        detail.setCompanyid(subCompanyId);
        detail.setSourceid(baseChannelDTO.getSource_id());
        detail.setMediaid(baseChannelDTO.getMedia_id());
        detail.setAccountid(baseChannelDTO.getAccount_id());
        detail.setChannelid(baseChannelDTO.getId());
        detail.setEffectiveDate(currentMonth);
        detail.setCustomerInfo(JSONObject.toJSONString(customerDTO));
        detail.setType(CommonConst.USER_MONTH_INFO_DETAIL_TYPE1);
        detail.setUserMonthInfoId(id);
        detail.setCustomerid(customerDTO.getId());
        userMonthInfoDetailMapper.insert(detail);
    }

    /**
     * 记录有效数.
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void incrNum2DB(CustomerInfo customerDto, Integer salesmanId) {

        Integer subCompanyId = customerDto.getSubCompanyId();
        String utmSource = customerDto.getUtmSource();
        String currentMonthStr = this.allocateRedisService.getMonthStr(CommonConst.USER_MONTH_INFO_MONTH_CURRENT);

        BaseChannelDTO baseChannelDTO = this.getChannelInfoByChannelName(utmSource);
        Date now = new Date();

        // 先查该记录
        UserMonthInfo e = new UserMonthInfo();
        e.setCompanyid(subCompanyId);
        e.setMediaid(baseChannelDTO.getMedia_id());
        e.setUserId(salesmanId);
        e.setEffectiveDate(currentMonthStr);
        e.setStatus(CommonEnum.entity_status1.getCode());
        List<UserMonthInfo> select = userMonthInfoMapper.findByParamsForUpdate(subCompanyId, baseChannelDTO.getMedia_id(), salesmanId, currentMonthStr, CommonEnum.entity_status1.getCode());

        Integer id = null;
        // 主表 incr
        if (CollectionUtils.isEmpty(select) || select.get(0) == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "数据异常，该客户已分配给了该业务员，但未找到分配数据（subCompanyId=" + subCompanyId + "，Mediaid=" + baseChannelDTO.getMedia_id() + "，salesmanId=" + salesmanId + "，currentMonth=" + currentMonthStr + "）");
        } else {
            UserMonthInfo userMonthInfo = select.get(0);
            userMonthInfo.setSourceid(baseChannelDTO.getSource_id());
            userMonthInfo.setAccountid(baseChannelDTO.getAccount_id());
            userMonthInfo.setChannelid(baseChannelDTO.getId());
            userMonthInfoMapper.update2IncrNumForEffectiveCustomerNum(userMonthInfo.getId());
            id = userMonthInfo.getId();
        }

        // 明细表记录明细
        UserMonthInfoDetail detail = new UserMonthInfoDetail();
        detail.setCreated(now);
        detail.setCompanyid(subCompanyId);
        detail.setSourceid(baseChannelDTO.getSource_id());
        detail.setMediaid(baseChannelDTO.getMedia_id());
        detail.setAccountid(baseChannelDTO.getAccount_id());
        detail.setChannelid(baseChannelDTO.getId());
        detail.setEffectiveDate(currentMonthStr);
        detail.setCustomerInfo(JSONObject.toJSONString(customerDto));
        detail.setType(CommonConst.USER_MONTH_INFO_DETAIL_TYPE2);
        detail.setUserMonthInfoId(id);
        detail.setCustomerid(customerDto.getId());
        userMonthInfoDetailMapper.insert(detail);
    }

    /**
     * 根据渠道获取渠道基本信息（目的获取来源id、媒体id）.
     */
    public BaseChannelDTO getChannelInfoByChannelName(String UtmSource) {
        JSONObject params = new JSONObject();
        params.put("channelName", UtmSource);
        TheaApiDTO<BaseChannelDTO> infoByChannelName = theaService.getInfoByChannelName(params);

        BaseChannelDTO result = new BaseChannelDTO();
        if (infoByChannelName.getResult() == 0 && infoByChannelName.getData() != null) {
            result = infoByChannelName.getData();
        }
        if (result == null || result.getSource_id() == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "Source_id 不能为null");
        }
        if (result.getMedia_id() == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "Media_id 不能为null");
        }
        if (result == null || result.getAccount_id() == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "getAccount_id 不能为null");
        }
        if (result == null || result.getId() == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "id 不能为null");
        }
        return result;
    }

    /**
     * 查某一级吧下某媒体的已分配数.
     */
    public FindMediaAssignedCustomerNumDTO findAssignedCustomerNum(FindMediaAssignedCustomerNumDTO params) {

        List<FindMediaAssignedCustomerNumItmDTO> list = params.getList();
        for (FindMediaAssignedCustomerNumItmDTO item : list) {
            Integer selectSum = userMonthInfoMapper.selectSum(item.getCompanyid(), item.getSourceid(), item.getMediaid(), item.getMonth(), CommonEnum.entity_status1.getCode());
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
            Integer selectSum = userMonthInfoMapper.selectSum(item.getCompanyid(), null, null, item.getMonth(), CommonEnum.entity_status1.getCode());
            Integer sum = selectSum;
            item.setAssigned_customer_num(sum == null ? 0 : sum);
        }
        return params;
    }

}
