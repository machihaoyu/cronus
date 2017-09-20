package com.fjs.cronus.service.redis;

import com.fjs.cronus.dto.cronus.UcUserDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by msi on 2017/9/19.
 */
@Service
public class LevallinkAgeRedisService {
    @Resource
    RedisTemplate<String,List> listOperations;
    @Resource
    RedisTemplate<String,Object> valueOperations;
    public void setLevallinkAgeInfo(String key,List idList){
        ValueOperations<String,List> redis = listOperations.opsForValue();
        redis.set(key,idList,30, TimeUnit.DAYS);
    }

    public List getLevallinkAgeInfo(String key) {
        ValueOperations<String,List> redis = listOperations.opsForValue();
        List idList = new ArrayList();
        idList = redis.get(key);
        return idList;
    }

//    public void setRedisUserInfo(String key, UcUserDto ucUserDto){
//        ValueOperations<String,Object> redis = valueOperations.opsForValue();
//        redis.set(key,ucUserDto,30, TimeUnit.SECONDS);
//    }
//    public UcUserDto getRedisUserInfo(String key) {
//        ValueOperations<String,Object> redis = valueOperations.opsForValue();
//        UcUserDto ucUserDto = (UcUserDto)redis.get(key);
//        return ucUserDto;
//    }
}
