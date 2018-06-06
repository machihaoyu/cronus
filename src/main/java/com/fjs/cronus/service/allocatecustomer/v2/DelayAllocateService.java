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
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
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
     * 处理延迟的queue.
     */
    private DelayQueue<DelayAllocateData> queue;

    /**
     * 工作线程.
     */
    private Thread worker;

    /**
     * 存到redis中的数据切割符号.
     */
    private String cutStr = "$";

    @PostConstruct
    public void initQueue() {

        this.queue = new DelayQueue<>();
        // 从redis获取未处理完的数据 or 因系统重启导致丢失的数据
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        List<String> data = listOperations.range(CommonRedisConst.ALLOCATE_DELAY, 0, -1);
        if (!CollectionUtils.isEmpty(data)) {
            for (String temp : data) {
                String[] str = temp.split(cutStr);
                String time = str[2];
                queue.put(new DelayAllocateData(temp, Long.valueOf(time)));
            }
        }

        worker = new Thread(new DelayAllocateTask());
        worker.start();
    }

    @PreDestroy
    public void close() {
        worker.interrupt();
    }

    /**
     * 将数据放入queue.
     */
    public void acceptData(String phone, Integer saleManid, Integer time, TimeUnit timeUnit) {
        // 参数校验
        if (StringUtils.isBlank(phone)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "phone 不能为null");
        }
        if (time == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "time 不能为null");
        }
        if (saleManid == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "saleManid 不能为null");
        }
        if (timeUnit == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "timeUnit 不能为null");
        }

        // 业务校验：是否在工作时间内
        long timeTemp = new Date().getTime() + timeUnit.toMillis(time);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        listOperations.rightPush(CommonRedisConst.ALLOCATE_DELAY, getDataStr(phone, saleManid, timeTemp));

        // queue.put(new DelayAllocateData(phone.trim(), timeTemp));
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

        Long lockToken = null;
        String key = null;
        try {
            key = CommonRedisConst.ALLOCATE_DELAY + "lock" + phone;
            lockToken = cRMRedisLockHelp.lockBySetNX(key);

            // 根据id
            CustomerDTO customer = ocdcServiceV2.getCustomer(phone);

            // 数据查询，看是否已和业务员沟通；已沟通则不管，未沟通则去分配队列给别的业务员.
            Integer id = customer.getId();

            CustomerInfo customerInfo = customerInfoService.findCustomerById(id);
            if (customerInfo != null && customerInfo.getCommunicateTime() != null) {

            }

            ListOperations<String, String> listOperations = redisTemplate.opsForList();

        } finally {
            cRMRedisLockHelp.unlockForSetNx2(key, lockToken);
        }
    }

    private String getDataStr(String phone, Integer salemanid, long delaytime){
        return phone.concat(cutStr).concat(salemanid.toString()).concat(cutStr).concat(String.valueOf(delaytime));
    }

}
