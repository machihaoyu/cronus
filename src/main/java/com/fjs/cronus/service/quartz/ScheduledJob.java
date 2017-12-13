package com.fjs.cronus.service.quartz;


import com.fjs.cronus.service.CommunicationLogService;
import com.fjs.cronus.service.CustomerMeetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/3 0003.
 * 增加定时沟通 定时面见的通知 前五分钟
 */
@Configuration
@Component // 此注解必加
@EnableScheduling // 此注解必加
public class ScheduledJob {

    @Autowired
    CommunicationLogService communicationLogService;
    @Autowired
    CustomerMeetService customerMeetService;
    private SimpleDateFormat dateFormat() {
        return new SimpleDateFormat("HH:mm:ss");
    }

    public void sayHello()  {
        System.out.println("AAAA: The time is now " + dateFormat().format(new Date()));
    }


    private static final Logger logger = LoggerFactory.getLogger(ScheduledJob.class);

    public void dotask() {
       logger.info("Examine Start!");
       //查询大于当前时间沟通和面见表获取沟通时间
        //调用任务
       // communicationLogService.sendMessToCustomer();
        //customerMeetService.sendMessMeetToCustomer();
        logger.info("Examine End!");
    }
}
