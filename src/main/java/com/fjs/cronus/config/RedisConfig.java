package com.fjs.cronus.config;

/**
 * Created by msi on 2017/9/18.
 */

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhoushengchun on 2017/9/15.
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private RedisTemplate redisTemplate;

    @SuppressWarnings("rawtypes")
    @Bean
    public CacheManager cacheManager() {
        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
        //设置缓存过期时间
        //rcm.setDefaultExpiration(60);//秒
        return rcm;
    }

    /**
     * 实例化 object 对象
     *
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> valueOperations() {
        RedisTemplate<String,Object> valueRedisTemplate = new RedisTemplate();
        valueRedisTemplate.setConnectionFactory(redisConnectionFactory);
        valueRedisTemplate.setKeySerializer(new StringRedisSerializer());
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        valueRedisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        valueRedisTemplate.afterPropertiesSet();
        return valueRedisTemplate;
    }

    /***
     * 实例化 hashOperations,可以使用 Object 操作
     * @return
     */

    @Bean
    public RedisTemplate<String,Map<String,Object>> hashOperations() {
        RedisTemplate<String,Map<String,Object>> hashRedisTemplate = new RedisTemplate();
        hashRedisTemplate.setConnectionFactory(redisConnectionFactory);
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        hashRedisTemplate.setKeySerializer(redisSerializer);
        hashRedisTemplate.setHashKeySerializer(redisSerializer);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        hashRedisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        hashRedisTemplate.afterPropertiesSet();

        return hashRedisTemplate;
    }


    /**
     * 实例化 ListOperations 对象,可以使用 List 操作
     *
     * @return
     */
    @Bean
    public RedisTemplate<String,List<Object>> listOperations() {
        RedisTemplate<String,List<Object>> listRedisTemplate = new RedisTemplate();
        listRedisTemplate.setConnectionFactory(redisConnectionFactory);
        listRedisTemplate.setKeySerializer(new StringRedisSerializer());
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        listRedisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        listRedisTemplate.afterPropertiesSet();
        return listRedisTemplate;
    }

    /**
     * 实例化 SetOperations 对象,可以使用 Set 操作
     *
     * @return
     */
    @Bean
    public  RedisTemplate<String,Set<Object>> setOperations() {
        RedisTemplate<String,Set<Object>> setRedisTemplate = new RedisTemplate();
        setRedisTemplate.setConnectionFactory(redisConnectionFactory);
        setRedisTemplate.setKeySerializer(new StringRedisSerializer());
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        setRedisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        setRedisTemplate.afterPropertiesSet();
        return setRedisTemplate;
    }

}
