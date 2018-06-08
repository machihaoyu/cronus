package com.fjs.cronus.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfigNew<T> {

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

    /**
     * 泛型的 redisTemplate 实例.
     *
     * 注意：放入内容类型 和 取出类型必须一致.
     * 例如：
     * ValueOperations<String, User> operations = valueRedisTemplate.opsForValue();
     * ListOperations<String, User> operations = valueRedisTemplate.opsForList();
     * HashOperations<String, User, User> operations = valueRedisTemplate.opsForHash();
     *
     * 建议：尽量让 java 类型和 redis 类型相匹配的使用
     * 例如：
     * ValueOperations<String, List<User>> operations = valueRedisTemplate.opsForValue();  // （反例说明），应该使用redis的集合类型处理java的集合类型；这里使用的是redis的value类型，本质上是将java序列化成字符串存放的
     * ListOperations<String, User> operations = valueRedisTemplate.opsForList(); // 推荐方式
     */
    @Bean
    public RedisTemplate<String, T> redisTemplateOps() {
        RedisTemplate<String,T> valueRedisTemplate = new RedisTemplate();

        valueRedisTemplate.setConnectionFactory(redisConnectionFactory);
        valueRedisTemplate.setKeySerializer(new StringRedisSerializer());

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        valueRedisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        valueRedisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
        valueRedisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        valueRedisTemplate.afterPropertiesSet();

        return valueRedisTemplate;
    }

}
