package com.fjs.cronus.service.redis;

import com.fjs.cronus.exception.CronusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class CRMRedisHelp {

    private static final Logger logger = LoggerFactory.getLogger(CRMRedisHelp.class);

    @Resource
    RedisTemplate<String, String> redisTemplate;

    /**
     * 上锁时间，秒.
     */
    private static final long TIMEOUT = 30 * 1000; // 秒

    /**
     * 设置redis 序列化器，解决setNX 成功但是expire失败情况.
     */
    private RedisTemplate getRedisTemplate() {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        return redisTemplate;
    }

    /**
     * 加锁.
     */
    public Long getLockBySetNX(String key) {
        // 重试次数
        Integer i = 0;

        // 锁时间
        Long currentTime = 0L;

        while (i < 3) {
            currentTime = this.getCurrentTimeFromRedisServicer() + TIMEOUT + 1;
            i++;
            try {
                if (this.setNX(key, currentTime.toString())) {
                    // 成功，需要设置timeout
                    Boolean expire = this.getRedisTemplate().expire(key, TIMEOUT, TimeUnit.MILLISECONDS);
                    if (!expire) {
                        this.unlockForSetNx(key, currentTime);
                        throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "设置锁timeout失败");
                    }
                    return currentTime;
                }
                TimeUnit.SECONDS.sleep(3);//睡眠3秒
            } catch (Exception e) {
                logger.error("获取redis锁异常", e);
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "获取redis锁异常，" + e.getMessage());
            }
        }
        throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "重试获取redis锁失败，请稍后请求");
    }

    /**
     * 解锁.
     */
    public void unlockForSetNx(String lockKey, long lockvalue) {
        Object currentTimeFromRedis = redisTemplate.opsForValue().get(lockKey);

        if (currentTimeFromRedis != null && Long.valueOf(currentTimeFromRedis.toString()).equals(lockvalue)) {
            // 如果是加锁者 则删除锁 如果不是则等待自动过期 重新竞争加锁
            redisTemplate.delete(lockKey);
        }
    }

    /**
     * redis setNx.
     */
    private Boolean setNX(String key, String value) {

        RedisCallback<Boolean> redisCallback = new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.setNX(key.getBytes(), value.getBytes());
            }
        };
        return (Boolean) this.getRedisTemplate().execute(redisCallback);
    }

    /**
     * redis 累加器.
     */
    public Long incr(String key) {

        RedisCallback<Long> redisCallback = new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                Long incr = connection.incr(key.getBytes());
                return incr;
            }
        };

        Object execute = this.getRedisTemplate().execute(redisCallback);
        return execute == null ? null : Long.valueOf(execute.toString());
    }

    /**
     * 从redis服务器获取当前时间(获取统一时间，避免服务器间时间不一致).
     */
    public Long getCurrentTimeFromRedisServicer() {
        RedisCallback redisCallback = new RedisCallback() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.time();
            }
        };

        Object result = this.getRedisTemplate().execute(redisCallback);
        if (result == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "从redis服务器获取当前时间异常，响应为null");
        }

        return (Long) result;
    }
}
