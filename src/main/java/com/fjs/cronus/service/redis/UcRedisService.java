package com.fjs.cronus.service.redis;

import com.fjs.cronus.dto.api.PHPLoginDto;
import com.fjs.cronus.dto.api.uc.AppUserDto;
import com.fjs.cronus.dto.cronus.UcUserDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by msi on 2017/9/18.
 */
@Service
public class UcRedisService {
    @Resource
    RedisTemplate<String,List> listOperations;
    @Resource
    RedisTemplate<String,Object> valueOperations;
    public void setRedisUcInfo(String key,List idList){
        ValueOperations<String,List> redis = listOperations.opsForValue();
        redis.set(key,idList,300, TimeUnit.SECONDS);
    }

    public List getRedisUcInfo(String key) {
        ValueOperations<String,List> redis = listOperations.opsForValue();
        List idList = new ArrayList();
        idList = redis.get(key);
        return idList;
    }

    public void setRedisUserInfo(String key, AppUserDto ucUserDTO){
        ValueOperations<String,Object> redis = valueOperations.opsForValue();
        redis.set(key, ucUserDTO,300, TimeUnit.SECONDS);
    }
    public AppUserDto getRedisUserInfo(String key) {
        ValueOperations<String,Object> redis = valueOperations.opsForValue();
        AppUserDto ucUserDTO = (AppUserDto)redis.get(key);
        return ucUserDTO;
    }

    public void setPhpUserInfo(String key,PHPLoginDto phpLoginDto){
        ValueOperations<String,Object> redis = valueOperations.opsForValue();
        redis.set(key, phpLoginDto,300, TimeUnit.SECONDS);
    }
    public PHPLoginDto getRedisPhpUserInfo(String key) {
        ValueOperations<String,Object> redis = valueOperations.opsForValue();
        PHPLoginDto ucUserDTO = (PHPLoginDto)redis.get(key);
        return ucUserDTO;
    }
}
