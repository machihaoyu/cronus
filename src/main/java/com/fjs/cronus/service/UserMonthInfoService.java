package com.fjs.cronus.service;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.UserMonthInfoMapper;
import com.fjs.cronus.model.UserMonthInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by feng on 2017/9/22.
 */
@Service
public class UserMonthInfoService {

    @Autowired
    private UserMonthInfoMapper userMonthInfoMapper;

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

        Calendar instance = Calendar.getInstance();
        Date currentMoth = instance.getTime();
        instance.add(Calendar.MONTH, 1);
        Date nextMoth = instance.getTime();

        String currentMothStr = sdf.format(currentMoth);
        String nextMothfStr = sdf.format(nextMoth);

        // 获取指定公司所有媒体下月分配数
        UserMonthInfo whereParams3 = new UserMonthInfo();
        whereParams3.setStatus(CommonEnum.entity_status1.getCode());
        whereParams3.setCompanyid(companyid);
        whereParams3.setEffectiveDate(nextMothfStr);
        List<UserMonthInfo> nextMonthAllDataList = userMonthInfoMapper.findByParams(whereParams3);

        Map<Integer, Integer> baseCustomerNumOfNextMonthCountMap = new HashMap<>();     // 下月总分配队列月分配数
        Map<Integer, Integer> rewardCustomerNumOfNextMonthCountMap = new HashMap<>();     // 下月总分配队列月奖励数
        Map<Integer, Integer> baseCustomerNumOfNextMonthMap = new HashMap<>();     // 下月其他媒体月分配数
        Map<Integer, Integer> rewardCustomerNumOfNextMonthMap = new HashMap<>(); // 下月的其他媒体月奖励数

        Boolean nextMonthHasData = false; // 下月是否已经有数据
        for (UserMonthInfo userMonthInfo : nextMonthAllDataList) {
            Object o;
            if (CommonConst.COMPANY_MEDIA_QUEUE_COUNT.equals(userMonthInfo.getMediaid())) {
                o = baseCustomerNumOfNextMonthCountMap.get(userMonthInfo.getMediaid()) == null ? null : baseCustomerNumOfNextMonthCountMap.put(userMonthInfo.getMediaid(), baseCustomerNumOfNextMonthCountMap.get(userMonthInfo.getMediaid()) + userMonthInfo.getBaseCustomerNum());
                o = rewardCustomerNumOfNextMonthCountMap.get(userMonthInfo.getMediaid()) == null ? null : rewardCustomerNumOfNextMonthCountMap.put(userMonthInfo.getMediaid(), rewardCustomerNumOfNextMonthCountMap.get(userMonthInfo.getMediaid()) + userMonthInfo.getRewardCustomerNum());
            } else if ( mediaId.equals(userMonthInfo.getMediaid())) {
                nextMonthHasData = true;
            } else {
                o = baseCustomerNumOfNextMonthMap.get(userMonthInfo.getMediaid()) == null ? null : baseCustomerNumOfNextMonthMap.put(userMonthInfo.getMediaid(), baseCustomerNumOfNextMonthMap.get(userMonthInfo.getMediaid()) + userMonthInfo.getBaseCustomerNum());
                o = rewardCustomerNumOfNextMonthMap.get(userMonthInfo.getMediaid()) == null ? null : rewardCustomerNumOfNextMonthMap.put(userMonthInfo.getMediaid(), rewardCustomerNumOfNextMonthMap.get(userMonthInfo.getMediaid()) + userMonthInfo.getRewardCustomerNum());
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
        this.insertList(list);
    }

}
