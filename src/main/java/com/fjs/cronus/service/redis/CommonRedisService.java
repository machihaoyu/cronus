package com.fjs.cronus.service.redis;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by msi on 2017/9/18.
 */
@Service
public class CommonRedisService {

    @Resource
    RedisTemplate<String,List> listOperations;

    @Resource
    RedisTemplate<String,Object> valueOperations;

//    @Resource
//    RedisTemplate<String,String> stringOperations;

    public void lockRedis(String key) {
        ValueOperations<String, Object> redis = valueOperations.opsForValue();
        Object sources = redis.get(key);
        if (!ObjectUtils.anyNotNull(sources)) {
            redis.set(key, key, 60, TimeUnit.MINUTES);
        }
    }

    public boolean existLock(String key) {
        ValueOperations<String, Object> redis = valueOperations.opsForValue();
        Object sources = redis.get(key);
        if (ObjectUtils.anyNotNull(sources)) {
            return true;
        }
        return false;
    }

}
