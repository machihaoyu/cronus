package com.fjs.cronus.service;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
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

    public void copyCurrentMonthDataToNexMonth(Integer updateUserId, Integer companyid, Integer mediaId) {

        Date now = new Date();
        // 获取当月、下月的effective_date

        String currentMothStr = allocateRedisService.getCurrentMonthStr();
        String nextMothfStr = allocateRedisService.getNextMonthStr();

        // 获取指定公司、所有有媒体、下月的分配数
        UserMonthInfo whereParams3 = new UserMonthInfo();
        whereParams3.setStatus(CommonEnum.entity_status1.getCode());
        whereParams3.setCompanyid(companyid);
        whereParams3.setEffectiveDate(nextMothfStr);
        List<UserMonthInfo> nextMonthAllDataList = userMonthInfoMapper.findByParams(whereParams3);

        Map<Integer, Integer> baseCustomerNumOfNextMonthCountMap = new HashMap<>();     // 下月总分配队列月分配数
        Map<Integer, Integer> rewardCustomerNumOfNextMonthCountMap = new HashMap<>();     // 下月总分配队列月奖励数
        Map<Integer, Integer> baseCustomerNumOfNextMonthMap = new HashMap<>();     // 下月其他媒体月分配数
        Map<Integer, Integer> rewardCustomerNumOfNextMonthMap = new HashMap<>(); // 下月的其他媒体月奖励数
        Object o; // 无用变量，便于三目运算符使用

        Boolean nextMonthHasData = false; // 下月是否已经有数据
        for (UserMonthInfo userMonthInfo : nextMonthAllDataList) {
            if (CommonConst.COMPANY_MEDIA_QUEUE_COUNT.equals(userMonthInfo.getMediaid())) {
                o = baseCustomerNumOfNextMonthCountMap.get(userMonthInfo.getUserId()) == null ? baseCustomerNumOfNextMonthCountMap.put(userMonthInfo.getUserId(), userMonthInfo.getBaseCustomerNum()) : baseCustomerNumOfNextMonthCountMap.put(userMonthInfo.getUserId(), baseCustomerNumOfNextMonthCountMap.get(userMonthInfo.getUserId()) + userMonthInfo.getBaseCustomerNum());
                o = rewardCustomerNumOfNextMonthCountMap.get(userMonthInfo.getUserId()) == null ? rewardCustomerNumOfNextMonthCountMap.put(userMonthInfo.getUserId(), userMonthInfo.getRewardCustomerNum()) : rewardCustomerNumOfNextMonthCountMap.put(userMonthInfo.getUserId(), rewardCustomerNumOfNextMonthCountMap.get(userMonthInfo.getUserId()) + userMonthInfo.getRewardCustomerNum());
            } else if (mediaId.equals(userMonthInfo.getMediaid())) {
                nextMonthHasData = true;
            } else {
                o = baseCustomerNumOfNextMonthMap.get(userMonthInfo.getMediaid()) == null ? baseCustomerNumOfNextMonthMap.put(userMonthInfo.getUserId(), userMonthInfo.getBaseCustomerNum()) : baseCustomerNumOfNextMonthMap.put(userMonthInfo.getUserId(), baseCustomerNumOfNextMonthMap.get(userMonthInfo.getUserId()) + userMonthInfo.getBaseCustomerNum());
                o = rewardCustomerNumOfNextMonthMap.get(userMonthInfo.getMediaid()) == null ? rewardCustomerNumOfNextMonthMap.put(userMonthInfo.getUserId(), userMonthInfo.getRewardCustomerNum()) : rewardCustomerNumOfNextMonthMap.put(userMonthInfo.getUserId(), rewardCustomerNumOfNextMonthMap.get(userMonthInfo.getUserId()) + userMonthInfo.getRewardCustomerNum());
            }
        }
        if (nextMonthHasData) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "下月此队列已有数据，不能再次复制");
        }
        if (baseCustomerNumOfNextMonthCountMap == null || baseCustomerNumOfNextMonthCountMap.size() == 0) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "总分配队列，下月的数据还未设置，请先设置");
        }

        // 获取指定公司、媒体当月分配数
        UserMonthInfo whereParams2 = new UserMonthInfo();
        whereParams2.setStatus(CommonEnum.entity_status1.getCode());
        whereParams2.setCompanyid(companyid);
        whereParams2.setMediaid(mediaId);
        whereParams2.setEffectiveDate(currentMothStr);
        List<UserMonthInfo> currentMothDataList = userMonthInfoMapper.findByParams(whereParams2);

        List<UserMonthInfo> list = new ArrayList<>();

        // 业务校验: 当月辅助当月的数据不能 > 总分配队列下月的数据
        for (UserMonthInfo userMonthInfo : currentMothDataList) {
            Integer baseCustomerNumOfNextMonthCount = baseCustomerNumOfNextMonthCountMap.get(userMonthInfo.getUserId()); // 下月总分配队列中，该用户的月分配数
            Integer rewardCustomerNumOfNextMonthCount = rewardCustomerNumOfNextMonthCountMap.get(userMonthInfo.getUserId()); // 下月总分配队列中，该用户月奖励数
            Integer baseCustomerNumOfNextMonthOtherMedia = baseCustomerNumOfNextMonthMap.get(userMonthInfo.getUserId()); // 下月其他媒体，该用户的月分配数
            Integer rewardCustomerNumOfNextMonthOtherMedia = rewardCustomerNumOfNextMonthMap.get(userMonthInfo.getUserId()); // 下月其他媒体，该用户月奖励数

            if (userMonthInfo.getBaseCustomerNum() > (baseCustomerNumOfNextMonthCount - baseCustomerNumOfNextMonthOtherMedia)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "用户id为" + userMonthInfo.getUserId() + "的用户，所复制的月分配数 > (总分配队列月分配数 - 其他媒体月分配数) ");
            }

            if (userMonthInfo.getRewardCustomerNum() > (rewardCustomerNumOfNextMonthCount - rewardCustomerNumOfNextMonthOtherMedia)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "用户id为" + userMonthInfo.getUserId() + "的用户，所复制的月分配数 > (总分配队列月分配数 - 其他媒体月分配数) ");
            }

            UserMonthInfo userMonthInfoTemp = new UserMonthInfo();
            userMonthInfoTemp.setBaseCustomerNum(userMonthInfo.getBaseCustomerNum());
            userMonthInfoTemp.setAssignedCustomerNum(userMonthInfo.getAssignedCustomerNum());
            userMonthInfoTemp.setEffectiveCustomerNum(userMonthInfo.getEffectiveCustomerNum());
            userMonthInfoTemp.setEffectiveDate(nextMothfStr);
            userMonthInfoTemp.setRewardCustomerNum(userMonthInfo.getRewardCustomerNum());
            userMonthInfoTemp.setLastUpdateTime(now);
            userMonthInfoTemp.setCreateTime(now);
            userMonthInfoTemp.setUserId(userMonthInfo.getUserId());
            userMonthInfoTemp.setCreateUserId(updateUserId);
            userMonthInfoTemp.setLastUpdateUser(updateUserId);
            userMonthInfoTemp.setCompanyid(userMonthInfo.getCompanyid());
            userMonthInfoTemp.setMediaid(userMonthInfo.getMediaid());
            list.add(userMonthInfoTemp);
        }

        // 数据入库
        o = CollectionUtils.isEmpty(list) ? null : this.insertList(list);
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void editUserMonthInfo(Integer loginUserId, Integer userId, Integer companyid, Integer mediaid, String effectiveDate, Integer baseCustomerNum, Integer rewardCustomerNum) {

        // redis锁
        // key结构：UserMonthInfo、companyid、mediaid
        String key = "UserMonthInfo".concat("$").concat(companyid.toString()).concat("$").concat(mediaid.toString());

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

            if (CommonConst.COMPANY_MEDIA_QUEUE_COUNT.equals(mediaid)) {
                // 总分配队列情况

                Integer rewardCustomerNumSum = 0;
                Integer baseCustomerNumSum = 0;
                for (UserMonthInfo userMonthInfoTemp : userAllMedialDataList) {
                    if (!CommonConst.COMPANY_MEDIA_QUEUE_COUNT.equals(userMonthInfoTemp.getMediaid())) {
                        // 获取所有特殊渠道 rewardCustomerNum 的和
                        rewardCustomerNumSum += userMonthInfoTemp.getRewardCustomerNum();
                        // 获取所有特殊渠道 baseCustomerNumSum 的和
                        baseCustomerNumSum += userMonthInfoTemp.getBaseCustomerNum();
                    }
                }

                if (rewardCustomerNum < rewardCustomerNumSum) {
                    throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "月奖励数 不能小于 其他特殊渠道月奖励数之和");
                }
                if (baseCustomerNum < baseCustomerNumSum) {
                    throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "月分配数 不能小于 其他特殊渠道月分配数之和");
                }

            } else {
                // 特殊分配队列情况

                Integer rewardCustomerNumSum = 0;
                Integer baseCustomerNumSum = 0;
                UserMonthInfo countMedia = null;
                UserMonthInfo currentMedia = null;
                for (UserMonthInfo userMonthInfoTemp : userAllMedialDataList) {
                    if (CommonConst.COMPANY_MEDIA_QUEUE_COUNT.equals(userMonthInfoTemp.getMediaid())) {
                        countMedia = userMonthInfoTemp;
                    } else if (mediaid.equals(userMonthInfoTemp.getMediaid())) {
                        currentMedia = userMonthInfoTemp;
                    } else {
                        // 获取除去总分配队列、当前分配队列外的特殊渠道 rewardCustomerNum 的和
                        rewardCustomerNumSum += userMonthInfoTemp.getRewardCustomerNum();
                        // 获取除去总分配队列、当前分配队列外的特殊渠道 baseCustomerNumSum 的和
                        baseCustomerNumSum += userMonthInfoTemp.getBaseCustomerNum();
                    }
                }

                if (rewardCustomerNum > (countMedia.getRewardCustomerNum() - rewardCustomerNumSum)) {
                    throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "月奖励数 不能大于 总分配队列-其他特殊渠道月奖励数后剩余的数");
                }
                if (baseCustomerNum > (countMedia.getBaseCustomerNum() - baseCustomerNumSum)) {
                    throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "月分配数 不能大于 总分配队列-其他特殊渠道月分配数后剩余的数");
                }
            }

            // 业务校验： 当月申请数需要 > 已分配数
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            Date targetDate = null;
            Date nowDate = null;

            try {
                targetDate = sdf.parse(effectiveDate);
                nowDate = sdf.parse(sdf.format(new Date()));
            } catch (ParseException e) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "effectiveDate 时间转换错误");
            }

            if (nowDate.compareTo(targetDate) <= 0) {
                // 当月需要校验，下月无需校验

                Map<String, Object> allocateMap = new HashMap<>();
                allocateMap.put("inOperation", CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_1.getCodeDesc() +
                        "," + CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_3.getCodeDesc());
                List<Integer> userIds = new ArrayList<>();
                userIds.add(userId);
                allocateMap.put("newOwnerIds", userIds);
                try {
                    allocateMap.put("createBeginDate", DateUtils.getBeginDateByStr(effectiveDate));
                    allocateMap.put("createEndDate", DateUtils.getEndDateByStr(effectiveDate));
                } catch (Exception e) {
                    throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "effectiveDate 时间转换错误");
                }
                allocateMap.put("operationsStr", CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_1.getCodeDesc() + "," + CommonEnum.ALLOCATE_LOG_OPERATION_TYPE_3.getCodeDesc());
                List<AllocateLog> allocateLogList = allocateLogService.selectByParamsMap(allocateMap);

                if (allocateLogList.size() >= (baseCustomerNum + rewardCustomerNum)) {
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
