package com.fjs.cronus.service;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.Common.CommonRedisConst;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.UserMonthInfoMapper;
import com.fjs.cronus.model.AllocateLog;
import com.fjs.cronus.model.UserMonthInfo;
import com.fjs.cronus.service.redis.AllocateRedisService;
import com.fjs.cronus.service.redis.CRMRedisLockHelp;
import com.fjs.cronus.util.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

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

    public List<UserMonthInfo> selectByParamsMap(Map<String, Object> map) {
        return userMonthInfoMapper.selectByParamsMap(map);
    }

    public Integer updateUserMonthInfo(UserMonthInfo whereParams, UserMonthInfo valueParams) {
        return userMonthInfoMapper.updateUserMonthInfo(whereParams, valueParams);
    }

    public Integer insertList(List<UserMonthInfo> userMonthInfoList) {
        return userMonthInfoMapper.insertList(userMonthInfoList);
    }

    public List<UserMonthInfo> findByParams(UserMonthInfo params) {
        return userMonthInfoMapper.findByParams(params);
    }

    public void copyCurrentMonthDataToNexMonth(Integer updateUserId, Integer companyid) {

        String key = CommonRedisConst.USERMONTHINFO_COPY.concat("$").concat(companyid.toString());
        Long lockToken = null;
        try {
            lockToken = cRMRedisLockHelp.lockBySetNX(key);

            Date now = new Date();
            Object o = null;
            // 获取当月、下月的effective_date

            String currentMothStr = allocateRedisService.getCurrentMonthStr();
            String nextMothfStr = allocateRedisService.getNextMonthStr();

            // 获取下月的分配数
            UserMonthInfo whereParams3 = new UserMonthInfo();
            whereParams3.setStatus(CommonEnum.entity_status1.getCode());
            whereParams3.setCompanyid(companyid);
            whereParams3.setEffectiveDate(nextMothfStr);
            List<UserMonthInfo> nextMonthAllDataList = userMonthInfoMapper.findByParams(whereParams3);
            nextMonthAllDataList = CollectionUtils.isEmpty(nextMonthAllDataList) ? new ArrayList<>() : nextMonthAllDataList;

            // 获取当月数据
            UserMonthInfo whereParams2 = new UserMonthInfo();
            whereParams2.setStatus(CommonEnum.entity_status1.getCode());
            whereParams2.setCompanyid(companyid);
            whereParams2.setEffectiveDate(currentMothStr);
            List<UserMonthInfo> currentMothDataList = userMonthInfoMapper.findByParams(whereParams2);
            currentMothDataList = CollectionUtils.isEmpty(currentMothDataList) ? new ArrayList<>() : currentMothDataList;

            // 业务校验：下月总队列数据 需要>= 这月总队列数据
            Map<Integer, List<UserMonthInfo>> nextMonthCountMap = nextMonthAllDataList.stream().filter(i -> CommonConst.COMPANY_MEDIA_QUEUE_COUNT.equals(i.getMediaid())).collect(groupingBy(UserMonthInfo::getUserId,toList()));
            List<UserMonthInfo> currentMonthCount = currentMothDataList.stream().filter(i -> CommonConst.COMPANY_MEDIA_QUEUE_COUNT.equals(i.getMediaid())).collect(toList());
            if (CollectionUtils.isEmpty(currentMonthCount)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "数据异常，未找到当月的总队列数据");
            }
            if (nextMonthCountMap != null && nextMonthCountMap.size() > 0) {
                for (UserMonthInfo currentMonth : currentMonthCount) {
                    List<UserMonthInfo> nextMonthList = nextMonthCountMap.get(currentMonth.getUserId());
                    if (CollectionUtils.isEmpty(nextMonthList) || nextMonthList.get(0) == null) {
                        throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "数据异常，在下月总队列中未找到业务员为 " + currentMonth.getUserId() + " 的数据");
                    }
                    UserMonthInfo nextMonthInfo = nextMonthList.get(0);
                    if (nextMonthInfo.getBaseCustomerNum() < currentMonth.getBaseCustomerNum()) {
                        throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "业务员为 " + currentMonth.getUserId() + " 下月总队列分配数 < 当月分配数");
                    }
                    if (nextMonthInfo.getRewardCustomerNum() < currentMonth.getRewardCustomerNum()) {
                        throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "业务员为 " + currentMonth.getUserId() + " 下月总队列奖励数 < 当月奖励数");
                    }
                }
            }

            // 准备入库数据
            List<UserMonthInfo> toDB = new ArrayList<>();
            List<Integer> mediaIds = new ArrayList<>();
            Set<String> nextMonthExsitData = nextMonthAllDataList.stream().map(i -> "下月已存在数据不用复制" + i.getMediaid() + "$" + i.getUserId()).collect(toSet());
            for (UserMonthInfo userMonthInfo : currentMothDataList) {
                String tempId = "下月已存在数据不用复制" + userMonthInfo.getMediaid() + "$" + userMonthInfo.getUserId();
                if (!nextMonthExsitData.contains(tempId)) {
                    userMonthInfo.setEffectiveDate(nextMothfStr);
                    userMonthInfo.setLastUpdateTime(now);
                    userMonthInfo.setLastUpdateUser(updateUserId);
                    userMonthInfo.setCreateTime(now);
                    userMonthInfo.setCreateUserId(updateUserId);
                    userMonthInfo.setAssignedCustomerNum(0);
                    userMonthInfo.setEffectiveCustomerNum(0);
                    toDB.add(userMonthInfo);
                    mediaIds.add(userMonthInfo.getMediaid());
                }
            }

            if ( CollectionUtils.isEmpty(toDB) ){
                this.insertList(toDB);
                // 拷贝redis队列
                for (Integer mediaId : mediaIds) {
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
    public void editUserMonthInfo(Integer loginUserId, Integer userId, Integer companyid, Integer mediaid, String effectiveDate, Integer baseCustomerNum, Integer rewardCustomerNum) {

        // 锁
        String key = CommonRedisConst.USERMONTHINFO_EDIT.concat("$").concat(companyid.toString()).concat("$").concat(mediaid.toString());

        Long lockToken = null;
        try {
            lockToken = cRMRedisLockHelp.lockBySetNX(key);
            // ===== 业务校验 =====
            // 总分配队列新增不用校验
            // 减少情况
            // 1、总分配队列的[月申请数]、[月奖励数] 不能分别 < 其他特殊渠道[月申请数]之和、[月奖励数]之和
            // 2、特殊渠道 [月申请数]、[月奖励数] 不能分别 >  (总分配队列[月申请数]、[月奖励数] 分别 - 剩余特殊渠道[月申请数]之和、[月奖励数]之和)
            UserMonthInfo params = new UserMonthInfo();
            params.setCompanyid(companyid);
            params.setUserId(userId);
            params.setEffectiveDate(effectiveDate);
            params.setStatus(CommonEnum.entity_status1.getCode());
            List<UserMonthInfo> userAllMedialDataList = this.findByParams(params); // 获取用户所以媒体、具体月份、具体吧的分配数据
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
            UserMonthInfo whereParams = new UserMonthInfo();
            whereParams.setUserId(userId);
            whereParams.setEffectiveDate(effectiveDate);
            whereParams.setCompanyid(companyid);
            whereParams.setMediaid(mediaid);
            whereParams.setStatus(CommonEnum.entity_status1.getCode());

            UserMonthInfo valueParams = new UserMonthInfo();
            valueParams.setLastUpdateUser(loginUserId);
            valueParams.setBaseCustomerNum(baseCustomerNum);
            valueParams.setRewardCustomerNum(rewardCustomerNum);
            valueParams.setLastUpdateTime(new Date());

            this.updateUserMonthInfo(whereParams, valueParams);

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

}
