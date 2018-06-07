package com.fjs.cronus.service.allocatecustomer.v2;

import com.fjs.cronus.Common.CommonRedisConst;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.service.CustomerInfoService;
import com.fjs.cronus.service.redis.CRMRedisLockHelp;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * 商机：15分钟未沟通，重新自动分配.
 */
@Service
public class DelayAllocateService {

    private final Logger logger = LoggerFactory.getLogger(DelayAllocateService.class);

    @Autowired
    private OcdcServiceV2 ocdcServiceV2;

    @Autowired
    private CRMRedisLockHelp cRMRedisLockHelp;

    @Autowired
    private CustomerInfoService customerInfoService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * queue.
     */
    private DelayQueue<DelayAllocateData> queue;

    /**
     * 工作线程.
     */
    private Thread worker;

    @PostConstruct
    public void initQueue() {

        this.queue = new DelayQueue<>();
        // 从redis获取未处理完的数据 or 因系统重启导致丢失的数据
        HashOperations<String, String, String> operater = getOperater();
        Map<String, String> entries = operater.entries(CommonRedisConst.ALLOCATE_DELAY);
        for (Map.Entry<String, String> s : entries.entrySet()) {
            String phone = s.getKey();
            String time = s.getValue();

            Date date = parseTime(time);
            // 忽略错误，丢掉此缓存数据；不要影响APP启动
            if (date != null) {
                Calendar now = Calendar.getInstance();
                now.add(Calendar.MINUTE, -15);
                Date time1 = now.getTime();

                if (time1.compareTo(date) > 0) {
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

    public boolean acceptData(String phone, Date time) {
        // 参数校验
        if (StringUtils.isBlank(phone)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "phone 不能为null");
        }
        phone = phone.trim();
        if (time == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "time 不能为null");
        }

        if (addData2Redis(phone, time)){
            queue.put(new DelayAllocateData(phone, time.getTime()));
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
    private void delayAllocate(String phone, long time) {
        ocdcServiceV2.delayAllocate(phone, time);
    }

    private Date parseTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
        Date parse = null;
        try {
            parse = sdf.parse(time);
        } catch (ParseException e) {
            logger.error("时间转换异常", e);
        }
        return parse;
    }

    private String parseTime(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
        return sdf.format(time);
    }

    /**
     * 将数据放入缓存.
     * 只放最新的
     */
    private Boolean addData2Redis(String phone, Date time) {
        HashOperations<String, String, String> operater = getOperater();

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

    public void deleteData(String phone, Date time){
        HashOperations<String, String, String> operater = getOperater();

        String s = operater.get(CommonRedisConst.ALLOCATE_DELAY, phone);
        Date time2 = parseTime(s);
        if (time2 != null && time.compareTo(time2) == 0) {
            operater.delete(CommonRedisConst.ALLOCATE_DELAY, phone);
        }
    }

    /**
     * 获取redis hash的操作器.
     */
    private HashOperations<String, String, String> getOperater() {
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        return hashOperations;
    }

}
