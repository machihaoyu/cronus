package com.fjs.cronus.service.quartz;


import com.fjs.cronus.service.*;
import com.fjs.cronus.service.allocatecustomer.v2.AutoAllocateServiceV2;
import com.fjs.cronus.service.allocatecustomer.v2.OcdcServiceV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    @Value("${token.current}")
    private String token;

    @Autowired
    private OcdcServiceV2 ocdcService;

    @Autowired
    private AutoAllocateServiceV2 autoAllocateService;

    @Autowired
    private AutoCleanService autoCleanService;

    @Resource
    private CustomerInfoService customerInfoService;

    @Autowired
    private DealgoService dealgoService;

    @Autowired
    private PanService panService;

    private SimpleDateFormat dateFormat() {
        return new SimpleDateFormat("HH:mm:ss");
    }

    public void sayHello()  {
        System.out.println("AAAA: The time is now " + dateFormat().format(new Date()));
    }


    private static final Logger logger = LoggerFactory.getLogger(ScheduledJob.class);

    public void dotask() {
        logger.info("ScheduledJob Start!");
        //查询大于当前时间沟通和面见表获取沟通时间
        //调用任务
        //communicationLogService.sendMessToCustomer(token);
        //customerMeetService.sendMessMeetToCustomer(token);

        logger.info("3.waitingPoolAllocate start");
        //ocdcService.waitingPoolAllocate(token);
        logger.info("3.waitingPoolAllocate end");
        //ocdcService.waitingPoolAllocate(token);

        //autoAllocateService.nonCommunicateAgainAllocate(token);

        logger.info("4.autoCleanTask start");
        //autoCleanService.autoCleanTask();
        logger.info("4.autoCleanTask end");

        //dealgo 接口数据
        logger.info("5.initProfileTask start");
        //dealgoService.initProfileTask();
        logger.info("5.initProfileTask end");

        logger.info("5.1.customersFromDiscardTask start");
        //panService.customersFromDiscardTask();
        logger.info("5.1.customersFromDiscardTask end");

//        logger.error("定时任务 ------> 新注册客户15天发送短信");
        logger.info("6.sandMessage start");
        //customerInfoService.sandMessage();
        logger.info("6.sandMessage end");

        logger.info("ScheduledJob End!");
    }
}
