package com.fjs.cronus.service.redis;

import com.fjs.cronus.dto.cronus.CallbackConfigDto;
import com.fjs.cronus.dto.cronus.UcUserDTO;
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
}
