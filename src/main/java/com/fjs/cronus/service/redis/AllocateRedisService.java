package com.fjs.cronus.service.redis;

import com.fjs.cronus.Common.CommonRedisConst;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.util.CommonUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 自动分配队列Redis服务
 * Created by feng on 2017/9/16.
 */
@Service
public class AllocateRedisService {


    @Resource
    RedisTemplate<String, String> redisAllocateTemplete;

    /**
     * 切割器.
     */
    private Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();

    /**
     * 连接器.
     */
    private Joiner joiner = Joiner.on(",").skipNulls();


    /**
     * 添加用户至城市自动分配队列中
     *
     * @param userId
     * @param city
     * @return
     */
    @Deprecated
    public String addUserToAllocateTemplete(Integer userId, String city) {
        if (null == userId || StringUtils.isBlank(city)) {
            return null;
        }
        //获取Redis中对应城市的分配列表
        String userIds = this.getAllocateTemplet(city);
        if (StringUtils.isBlank(userIds)) {
            userIds = userId.toString();
        } else {//拼接新的队列value
            //先判断添加userID 是否包含在原队列中
            String[] userIdsArray = userIds.split(",");
            if (!ArrayUtils.contains(userIdsArray, userId.toString())) {
                userIds = userIds + "," + userId.toString();
            }
        }
        this.setAllocateTemplete(userIds, city);
        return userIds;
    }

    /**
     * 设置城市分配队列
     *
     * @param userIds
     * @param city
     */
    @Deprecated
    public String setAllocateTemplete(String userIds, String city) {
        redisAllocateTemplete.setValueSerializer(new StringRedisSerializer());
        ValueOperations<String, String> redisAllocateOptions = redisAllocateTemplete.opsForValue();
        redisAllocateOptions.set(CommonRedisConst.ALLOCATE_LIST + city, userIds);
        return userIds;
    }

    /**
     * 移除用户至城市自动分配队列中
     *
     * @param userId
     * @param city
     * @return
     */
    public String delUserToAllocateTemplete(Integer userId, String city) {
        //从Redis中取参数
        String userIds = this.getAllocateTemplet(city);
        if (StringUtils.isBlank(userIds)) {
            return null;
        } else {
            String[] userIdsArray = userIds.split(",");
            userIdsArray = ArrayUtils.removeElement(userIdsArray, userId.toString());
            userIds = "";
            if (userIdsArray.length == 0) {//新队列参数为空则移除队列
                this.removeAllocateTemplet(CommonRedisConst.ALLOCATE_LIST + city);
            } else {//新队列参数不为空，则分情况重新设置
                userIds = CommonUtil.queryStrArrayToStr(userIdsArray);
            }
        }
        this.setAllocateTemplete(userIds, city);
        return userIds;
    }

    /**
     * 自动分配修改队列
     *
     * @param city
     * @return
     */
    public String changeAllocateTemplet(Integer userId, String city) {
        String userIds = this.getAllocateTemplet(city);
        if (StringUtils.isBlank(userIds)) {
            return null;
        } else {
            String[] userIdsArray = userIds.split(",");
            String[] newUserIdsArray = null;
            String userIdStr = userId.toString();
            for (int i = 0; i < userIdsArray.length; i++) {
                if (userIdStr.equals(userIdsArray[i])) {
                    newUserIdsArray = ArrayUtils.remove(userIdsArray, i);
                    break;
                }
            }
//            userIdsArray = ArrayUtils.remove(userIdsArray,0);
            newUserIdsArray = ArrayUtils.add(newUserIdsArray, userIdStr);
            //拼接
            userIds = CommonUtil.queryStrArrayToStr(newUserIdsArray);
            userIds = this.setAllocateTemplete(userIds, city);
        }
        return userIds;
    }

    /**
     * 移除城市自动分配队列
     *
     * @param city
     */
    @Deprecated
    public void removeAllocateTemplet(String city) {
        redisAllocateTemplete.delete(CommonRedisConst.ALLOCATE_LIST + city);
    }

    /**
     * 从Redis中根据城市取队列
     *
     * @param city
     * @return
     */
    public String getAllocateTemplet(String city) {
        redisAllocateTemplete.setValueSerializer(new StringRedisSerializer());
        ValueOperations<String, String> redisAllocateOptions = redisAllocateTemplete.opsForValue();
        String userIds = redisAllocateOptions.get(CommonRedisConst.ALLOCATE_LIST + city);
        return userIds;
    }

    /**
     * 添加用户到指定公司、指定媒体、指定月的队列中；从队列尾部添加.
     */
    public String addUserToAllocateTemplete2(Integer userId, Integer companyId, Integer medial, String effectiveDate) {
        if (userId == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "userId 不能为null");
        }

        String userIds = this.getAllocateTemplet2(companyId, medial, effectiveDate);

        List<String> uiserIdList = StringUtils.isBlank(userIds) ? new ArrayList<>() : this.splitter.splitToList(userIds);
        if (!uiserIdList.contains(userId.toString())) uiserIdList.add(userId.toString());

        String newValue = this.joiner.join(uiserIdList);

        this.setAllocateTemplete2(newValue,  companyId,  medial,  effectiveDate);
        return newValue;
    }

    /**
     * 从队列移除用户
     */
    public String delUserToAllocateTemplete2(Integer userId, Integer companyId, Integer medial, String effectiveDate) {
        if (userId == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "userId 不能为null");
        }

        String userIds = this.getAllocateTemplet2(companyId, medial, effectiveDate);
        List<String> uiserIdList = StringUtils.isBlank(userIds) ? new ArrayList<>() : this.splitter.splitToList(userIds);
        if (uiserIdList.contains(userId.toString())) uiserIdList.remove(userId.toString());

        String newValue = this.joiner.join(uiserIdList);

        this.setAllocateTemplete2(newValue,  companyId,  medial,  effectiveDate);
        return newValue;
    }

    /**
     * 从队列中获取.
     */
    public List<Integer> finaAllFromQueue(Integer companyId, Integer medial, String effectiveDate){
        String userIds = this.getAllocateTemplet2(companyId, medial, effectiveDate);
        return StringUtils.isBlank(userIds) ? new ArrayList<>() : this.splitter.splitToList(userIds).stream().map(Integer::valueOf).collect(Collectors.toList());
    }

    private String getCompanyMediaQueueRedisKey(Integer companyId, Integer medial, String effectiveDate) {
        if (companyId == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "获取队列key错误，companyId 不能为null");
        }
        if (medial == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "获取队列key错误，medial 不能为null");
        }
        if (StringUtils.isBlank(effectiveDate)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "获取队列key错误，effectiveDate 不能为null");
        }
        // key结构：指定公司、指定媒体、指定月
        return CommonRedisConst.ALLOCATE_LIST.concat("$").concat(companyId.toString()).concat("$").concat(medial.toString()).concat("$").concat(effectiveDate);
    }

    private String getAllocateTemplet2(Integer companyId, Integer medial, String effectiveDate) {
        String key = this.getCompanyMediaQueueRedisKey(companyId, medial, effectiveDate);
        redisAllocateTemplete.setValueSerializer(new StringRedisSerializer());
        ValueOperations<String, String> redisAllocateOptions = redisAllocateTemplete.opsForValue();
        return redisAllocateOptions.get(key);
    }

    private String setAllocateTemplete2(String value, Integer companyId, Integer medial, String effectiveDate) {
        String key = this.getCompanyMediaQueueRedisKey(companyId, medial, effectiveDate);
        redisAllocateTemplete.setValueSerializer(new StringRedisSerializer());
        ValueOperations<String, String> redisAllocateOptions = redisAllocateTemplete.opsForValue();
        redisAllocateOptions.set(key, value);
        redisAllocateOptions.set(key, value, 90, TimeUnit.DAYS);
        return value;
    }
}
