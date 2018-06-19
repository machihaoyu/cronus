package com.fjs.cronus.service.allocatecustomer.v2;

import com.fjs.cronus.Common.CommonRedisConst;
import com.fjs.cronus.exception.CronusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.DelayQueue;

/**
 * 商机：15分钟未沟通，重新自动分配.
 */
@Service
public class DelayAllocateService {

    private final Logger logger = LoggerFactory.getLogger(DelayAllocateService.class);

    @Autowired
    private AutoAllocateServiceV2 autoAllocateServiceV2;

    @Resource
    RedisTemplate redisTemplateOps;
    /**
     * delay的时间.
     */
    public static int savetime = 15; // 分钟

    /**
     * queue.
     */
    private DelayQueue<DelayAllocateData> queue;

    /**
     * 工作线程.
     */
    private Thread worker;

    /**
     * redis 原子性 删除15分钟未沟通手机，且保证时间是相等
     */
    private String script = "local timeStr = redis.call('HGET', KEYS[1], ARGV[1]);\n" +
            "if timeStr and ARGV[2] then\n" +
            "if ( timeStr == ARGV[2] ) then\n" +
            "redis.call('HDEL', KEYS[1], ARGV[1])" +
            "return timeStr;\n" +
            "end;\n" +
            "end\n" +
            "return nil;";

    @PostConstruct
    public void initQueue() {

        this.queue = new DelayQueue<>();
        // 从redis获取未处理完的数据 or 因系统重启导致丢失的数据
        HashOperations<String, Number, String> operater = redisTemplateOps.opsForHash();
        Map<Number, String> entries = operater.entries(CommonRedisConst.ALLOCATE_DELAY);
        for (Map.Entry<Number, String> s : entries.entrySet()) {
            Long phone = s.getKey().longValue(); // 此处有坑；这里redisTemplate序列化使用的是Jackson解析器，在数字处于Integer范围内时，返回的是Integer，与Long冲突，所以使用Number类型，在转成long.
            String time = s.getValue();

            Date date = parseTime(time);
            // 忽略错误，丢掉此缓存数据；不要影响APP启动
            if (date != null) {
                if (new Date().compareTo(date) > 0) {
                    operater.delete(CommonRedisConst.ALLOCATE_DELAY, phone);
                } else {
                    // 添加最近15分钟的数据
                    queue.put(new DelayAllocateData(phone, date.getTime()));
                }
            } else {
                operater.delete(CommonRedisConst.ALLOCATE_DELAY, phone);
            }
        }

        worker = new Thread(new DelayAllocateTask());
        worker.start();
    }

    @PreDestroy
    public void close() {
        worker.interrupt();
    }

    boolean acceptData(Long phone, Date time) {
        // 注意这里的phone使用的是long类型，是因为这里的redisTemplate的序列化器使用的是Jackson，如果使用phone是String类型，在putIfAbsent会出现bug现象.
        // 参数校验
        if (phone == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "phone 不能为null");
        }
        if (time == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "time 不能为null");
        }

        if (addData2Redis(phone, time)){
            DelayAllocateData delayAllocateData = new DelayAllocateData(phone, time.getTime());
            queue.remove(delayAllocateData); // 如有重复的，则删除老的，放入新的
            queue.put(delayAllocateData);
            return true;
        }
        return false;
    }

    /**
     * 工作线程类.
     */
    private class DelayAllocateTask implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    DelayAllocateData task = queue.take();
                    new Thread(() -> {
                        delayAllocate(task.getData(), task.getDelaytime());
                    }).start();
                } catch (InterruptedException e) {
                    logger.error("延迟分配异常(处理15分钟未沟通)", e);
                }
            }
        }
    }

    /**
     * 处理15分钟未处理重新分配业务.
     */
    private void delayAllocate(Long phone, long time) {
        autoAllocateServiceV2.delayAllocate(phone, time);
    }

    private Date parseTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = null;
        try {
            parse = sdf.parse(time);
        } catch (ParseException e) {
            logger.error("时间转换异常", e);
        }
        return parse;
    }

    private String parseTime(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time);
    }

    /**
     * 将数据放入缓存.
     * 只放最新的
     */
    private Boolean addData2Redis(Long phone, Date time) {
        HashOperations<String, Long, String> operater = redisTemplateOps.opsForHash();

        Boolean aBoolean = operater.putIfAbsent(CommonRedisConst.ALLOCATE_DELAY, phone, parseTime(time));
        if (!aBoolean) {
            String s = operater.get(CommonRedisConst.ALLOCATE_DELAY, phone);
            Date db = parseTime(s);
            if (db != null && time.compareTo(db) > 0) {
                operater.delete(CommonRedisConst.ALLOCATE_DELAY, phone);
                aBoolean = operater.putIfAbsent(CommonRedisConst.ALLOCATE_DELAY, phone, parseTime(time));
            }
        }
        return aBoolean;
    }

    void deleteData(Long phone, Date time){
        HashOperations<String, Long, String> operater = redisTemplateOps.opsForHash();

        String s = operater.get(CommonRedisConst.ALLOCATE_DELAY, phone);
        Date time2 = parseTime(s);
        if (time2 != null && time.compareTo(time2) == 0) {
            operater.delete(CommonRedisConst.ALLOCATE_DELAY, phone);
        }
    }

    private RedisScript getRedisScript(String scriptStr) {
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

    private void delete(String key, long phone, String timeStr){
        // 此处有坑，由于redis的序列化器导致，redis中存在time是"2018-06-14 16:23:09"，所以需要再加引号
        timeStr = "\"" + timeStr + "\"";
        redisTemplateOps.execute(getRedisScript(script), new StringRedisSerializer(), new StringRedisSerializer(), Collections.singletonList(key), String.valueOf(phone), timeStr);
    }

}
