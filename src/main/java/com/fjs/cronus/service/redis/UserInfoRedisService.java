package com.fjs.cronus.service.redis;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonRedisConst;
import com.fjs.cronus.api.PhpApiDto;
import com.fjs.cronus.service.client.ThorInterfaceService;
import com.fjs.cronus.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 *
 * 用户信息Redis服务
 * Created by feng on 2017/10/24.
 */
@Service
public class UserInfoRedisService {

    @Value("${token.current}")
    private String publicToken;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    RedisTemplate<String,Map<String, String>> redisUserInfoInfoTemplate;

    /**
     * 获取用户所有下属业务员的IDS
     */
//    @Autowired
//    ThorUcService thorUcService;

    @Autowired
    private ThorInterfaceService thorInterfaceService;

    public String getSubUserIds(String token,Integer userId){
        ValueOperations<String, String> redisOptions = stringRedisTemplate.opsForValue();
        String userIds = redisOptions.get(CommonRedisConst.SUB_USER_IDS+userId);
        if (StringUtils.isNotBlank(userIds)) {
            return userIds;
        } else {
            PhpApiDto<List<String>> phpApiDto = thorInterfaceService.getSubUserByUserId(token, userId,CommonConst.SYSTEMNAME,4);
            if (null != phpApiDto.getRetData() && phpApiDto.getRetData().size() > 0) {
                userIds = CommonUtil.initStrListToStr(phpApiDto.getRetData());
                this.setSebUserIds(userId,userIds);
            }
        }
        return userIds;
    }

    /**
     * Redis设置保存业务员的IDS
     */
    public void setSebUserIds(Integer userId, String userIds) {
        ValueOperations<String, String> redisOptions = stringRedisTemplate.opsForValue();
        redisOptions.set(CommonRedisConst.SUB_USER_IDS+userId, userIds,CommonRedisConst.SUB_USER_IDS_TIMEOUT, TimeUnit.SECONDS);
    }


}
