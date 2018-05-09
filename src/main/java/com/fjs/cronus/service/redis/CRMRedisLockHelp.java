package com.fjs.cronus.service.redis;

import com.fjs.cronus.exception.CronusException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
public class CRMRedisLockHelp {

    private static final Logger logger = LoggerFactory.getLogger(CRMRedisLockHelp.class);

    @Resource
    RedisTemplate<String, String> redisTemplate;

    /**
     * 加锁脚本；setNX 命令对应的 lua 脚本.
     */
    private static final String SETNX_LUA_SCRIPT = "return redis.call('SET', KEYS[1], ARGV[1], 'NX', 'PX', ARGV[2])";

    /**
     * 解锁脚本.
     */
    private static final String UNLOCK_LU_SCRIPT = "if (redis.call('GET', KEYS[1]) == ARGV[1]) then return redis.call('DEL',KEYS[1]) else return 0 end";

    /**
     * 设置redis 序列化器，解决setNX 成功但是expire失败情况.
     */
    private RedisTemplate getRedisTemplate() {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        return redisTemplate;
    }

    private RedisScript getRedisScript(String scriptStr) {
        // 脚本对象
        return new RedisScript<String>() {
            @Override
            public String getSha1() {
                return DigestUtils.sha1DigestAsHex(scriptStr);
            }

            @Override
            public Class<String> getResultType() {
                return String.class;
            }

            @Override
            public String getScriptAsString() {
                return scriptStr;
            }
        };
    }

    /**
     * 加锁.
     */
    public Long lockBySetNX(String key) {

        Integer i = 0; // 重试次数
        Long lockToken = 0L; // 标记，用于解锁
        long  timeout = 60 * 1000; // 60秒

        while (i < 5) {
            lockToken = this.getCurrentTimeFromRedisServicer() + timeout + 1;
            i++;
            if (this.lockBySetNX(key, lockToken.toString(), timeout, this.getRedisScript(SETNX_LUA_SCRIPT)))
                return lockToken;
            try {
                TimeUnit.SECONDS.sleep(3);// 睡眠3秒
            } catch (InterruptedException e) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "设置睡眠异常" + e.getMessage());
            }
        }
        throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "redis 获取分布式锁失败");
    }


    /**
     * 加锁:通过redis lua 脚本方式实现加锁.
     * <p>
     * 解释：通过setNX方式，且设过期时间，保证原子性.
     */
    private boolean lockBySetNX(String key, String value, long timeout, RedisScript<String> redisScript) {
        if (redisScript == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "redisScript 不能为空");
        }
        if (StringUtils.isEmpty(key)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "redis 缓存 key 不能为空");
        }
        if (StringUtils.isEmpty(value)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "redis 缓存 value 不能为空");
        }
        if (timeout < 1000) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "redis 缓存 timeout 时间太短");
        }

        // 执行脚本
        Object result = this.getRedisTemplate().execute(redisScript, new StringRedisSerializer(), new StringRedisSerializer(), Collections.singletonList(key), value, String.valueOf(timeout));
        if ("OK".equals(result)) {
            // 成功，返回 OK
            return true;
        }
        //失败，返回 null
        return false;
    }

    /**
     * 解锁.
     */
    public boolean unlockForSetNx(String lockKey, String flag) {
        return this.unlockForSetNx(lockKey, flag, this.getRedisScript(UNLOCK_LU_SCRIPT));
    }

    /**
     * 解锁.
     */
    public void unlockForSetNx2(String lockKey, Long lockToken) {
        if (StringUtils.isNotBlank(lockKey) && lockToken != null) {
            this.unlockForSetNx(lockKey, lockToken.toString());
        }
    }

    /**
     * 解锁:通过redis lua 脚本方式实现解锁.
     */
    private boolean unlockForSetNx(String key, String lockToken, RedisScript<String> redisScript) {
        if (redisScript == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "redisScript 不能为空");
        }
        if (StringUtils.isEmpty(key)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "redis 缓存 key 不能为空");
        }
        if (StringUtils.isEmpty(lockToken)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "redis 缓存 unlockFlag 不能为空");
        }

        // 执行脚本
        Object result = this.getRedisTemplate().execute(redisScript, new StringRedisSerializer(), new StringRedisSerializer(), Collections.singletonList(key), lockToken);
        if (result !=null && "1".equals(result.toString())) {
            // 成功，返回 1
            return true;
        }
        // 失败，返回 0
        return false;
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
