package com.fjs.cronus.service.redis;

import com.fjs.cronus.dto.cronus.RedisSubUserInfoDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by msi on 2017/10/11.
 */
@Service
public class CronusRedisService {


    @Resource
    RedisTemplate<String,List> listOperations;

    @Resource
    RedisTemplate<String,Object> valueOperations;

    public void setRedisCronusInfo(String key,List list){
        ValueOperations<String,List> redis = listOperations.opsForValue();
        redis.set(key,list,30, TimeUnit.DAYS);
    }

    public List getRedisCronusInfo(String key) {
        ValueOperations<String,List> redis = listOperations.opsForValue();
        List list = new ArrayList();
        list = redis.get(key);
        return list;
    }


    /**
     * 设置未沟通分配失败的客户
     * @param key
     * @param list
     */
    public void setRedisFailNonConmunicateAllocateInfo(String key,List list){
        ValueOperations<String,List> redis = listOperations.opsForValue();
        redis.set(key,list,30, TimeUnit.DAYS);
    }

    /**
     * 获取未沟通分配失败的客户
     * @param key
     * @return
     */
    public List getRedisFailNonConmunicateAllocateInfo(String key) {
        ValueOperations<String,List> redis = listOperations.opsForValue();
        List list = new ArrayList();
        list = redis.get(key);
        return list;
    }

    /**
     * 清空未沟通分配失败的客户
     * @param key
     */
    public void clearRedisFailNonConmunicateAllocateInfo(String key){
        ValueOperations<String,List> redis = listOperations.opsForValue();
        redis.set(key,null,30, TimeUnit.DAYS);
    }

    public void setRedisDocumentInfo(String key,List list){
        ValueOperations<String,List> redis = listOperations.opsForValue();
        redis.set(key,list,360, TimeUnit.SECONDS);
    }

    public List getRedisDocumentInfo(String key) {
        ValueOperations<String,List> redis = listOperations.opsForValue();
        List list = new ArrayList();
        list = redis.get(key);
        return list;
    }

    public void setRedisSubUserInfo(String key,RedisSubUserInfoDTO list){
        ValueOperations<String,Object> redis = valueOperations.opsForValue();
        redis.set(key,list,360, TimeUnit.SECONDS);
    }

    public RedisSubUserInfoDTO getRedisSubUserInfo(String key) {
        ValueOperations<String,Object> redis = valueOperations.opsForValue();
        RedisSubUserInfoDTO redisSubUserInfoDTO = new RedisSubUserInfoDTO();
        redisSubUserInfoDTO = (RedisSubUserInfoDTO)redis.get(key);
        return redisSubUserInfoDTO;
    }

}
