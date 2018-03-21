package com.fjs.cronus.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/28 0028.
 */
@Configuration
@EnableCaching
public class RedisConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    /**
     * 缓存管理器.
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
        CacheManager cacheManager = new RedisCacheManager(redisTemplate);
        return cacheManager;
    }

    @Bean
    public RedisTemplate<String, String> redisConfigTemplete(RedisConnectionFactory factory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(factory);
        //key序列化方式;（不然会出现乱码;）,但是如果方法上有Long等非String类型的话，会报类型转换错误；
        //所以在没有自己定义key生成策略的时候，以下这个代码建议不要这么写，可以不配置或者自己实现ObjectRedisSerializer
        //或者JdkSerializationRedisSerializer序列化方式;
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();//Long类型不可以会出现异常信息;
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);

        return redisTemplate;
    }



    @Bean
    public RedisTemplate<String, List<String>> redisAllocateTemplete() {
        RedisTemplate<String, List<String>> redisTemplate = new RedisTemplate<String, List<String>>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //key序列化方式;（不然会出现乱码;）,但是如果方法上有Long等非String类型的话，会报类型转换错误；
        //所以在没有自己定义key生成策略的时候，以下这个代码建议不要这么写，可以不配置或者自己实现ObjectRedisSerializer
        //或者JdkSerializationRedisSerializer序列化方式;
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();//Long类型不可以会出现异常信息;
        redisTemplate.setKeySerializer(redisSerializer);


//        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());

//        redisTemplate.setHashValueSerializer(redisSerializer);
        return redisTemplate;
    }


    @Bean
    public RedisTemplate<String,List<String>> listStringOperations() {
        RedisTemplate<String,List<String>> listStringRedisTemplate = new RedisTemplate();
        listStringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        listStringRedisTemplate.setKeySerializer(new StringRedisSerializer());
            /*Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
            ObjectMapper om = new ObjectMapper();
            om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            jackson2JsonRedisSerializer.setObjectMapper(om);
            listStringRedisTemplate.setValueSerializer(jackson2JsonRedisSerializer);*/
        listStringRedisTemplate.afterPropertiesSet();
        return listStringRedisTemplate;
    }


    @Bean
    public RedisTemplate<String,List<Integer>> listIntegerOperations() {
        RedisTemplate<String,List<Integer>> listStringRedisTemplate = new RedisTemplate();
        listStringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        listStringRedisTemplate.setKeySerializer(new StringRedisSerializer());
            /*Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
            ObjectMapper om = new ObjectMapper();
            om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            jackson2JsonRedisSerializer.setObjectMapper(om);
            listStringRedisTemplate.setValueSerializer(jackson2JsonRedisSerializer);*/
        listStringRedisTemplate.afterPropertiesSet();
        return listStringRedisTemplate;
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
     * 实例化 object 对象
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

    @Bean
    public RedisTemplate<String,Map<String, String>> redisSystemDataAuthTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String,Map<String, String>> redisTemplate = new RedisTemplate<String,Map<String, String>>();

        redisTemplate.setConnectionFactory(factory);

        //key序列化方式;（不然会出现乱码;）,但是如果方法上有Long等非String类型的话，会报类型转换错误；
        //所以在没有自己定义key生成策略的时候，以下这个代码建议不要这么写，可以不配置或者自己实现ObjectRedisSerializer
        //或者JdkSerializationRedisSerializer序列化方式;
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();//Long类型不可以会出现异常信息;
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());

        return redisTemplate;
    }


}