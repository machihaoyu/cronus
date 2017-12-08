package com.fjs.cronus.service.redis;

import com.fjs.cronus.Common.CommonRedisConst;
import com.fjs.cronus.util.CommonUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 自动分配队列Redis服务
 * Created by feng on 2017/9/16.
 */
@Service
public class AllocateRedisService {


    @Resource
    RedisTemplate<String, String> redisAllocateTemplete;


    /**
     * 添加用户至城市自动分配队列中
     *
     * @param userId
     * @param city
     * @return
     */
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
     * @param city
     * @return
     */
    public String changeAllocateTemplet(Integer userId ,String city){
        String userIds = this.getAllocateTemplet(city);
        if(StringUtils.isBlank(userIds)){
            return null;
        } else {
            String[] userIdsArray = userIds.split(",");
            String userIdStr = userId.toString();
            for (int i = 0 ; i<= userIdsArray.length; i++) {
                if (userIdStr.equals(userIdsArray[i])) {
                    ArrayUtils.remove(userIdsArray, i);
                }
            }
            userIdsArray = ArrayUtils.remove(userIdsArray,0);
            userIdsArray = ArrayUtils.add(userIdsArray, userIdStr);
            //拼接
            userIds = CommonUtil.queryStrArrayToStr(userIdsArray);
            userIds = this.setAllocateTemplete(userIds, city);
        }
        return userIds;
    }

    /**
     * 移除城市自动分配队列
     *
     * @param city
     */
    public void removeAllocateTemplet(String city) {
        redisAllocateTemplete.delete(CommonRedisConst.ALLOCATE_LIST + city);
    }

    /**
     * 从Redis中根据城市取队列
     * @param city
     * @return
     */
    public String getAllocateTemplet(String city){
        redisAllocateTemplete.setValueSerializer(new StringRedisSerializer());
        ValueOperations<String, String> redisAllocateOptions = redisAllocateTemplete.opsForValue();
        String userIds = redisAllocateOptions.get(CommonRedisConst.ALLOCATE_LIST + city);
        return userIds;
    }
}
