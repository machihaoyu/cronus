package com.fjs.cronus.service.quartz;


import com.fjs.cronus.service.*;
import com.fjs.cronus.service.allocatecustomer.v1.AutoAllocateService;
import com.fjs.cronus.service.allocatecustomer.v1.OcdcService;
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
    private OcdcService ocdcService;

    @Autowired
    private AutoAllocateService autoAllocateService;

    @Autowired
    private AutoCleanService autoCleanService;

    @Resource
    private CustomerInfoService customerInfoService;

    @Autowired
    private DealgoService dealgoService;

    @Autowired
    private PanService panService;

    @Autowired
    private EzucQurtzService ezucQurtzService;

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
        new Thread(()->{
            communicationLogService.sendMessToCustomer(token);
        }).start();
        new Thread(()->{
            customerMeetService.sendMessMeetToCustomer(token);
        }).start();

        logger.info("4.autoCleanTask start");
        new Thread(()->{
            autoCleanService.autoCleanTask();
        }).start();
        logger.info("4.autoCleanTask end");

        //dealgo 接口数据
        logger.info("5.initProfileTask start");
        new Thread(()->{
            dealgoService.initProfileTask();
        }).start();
        logger.info("5.initProfileTask end");

        logger.info("5.1.customersFromDiscardTask start");
        new Thread(()->{
            panService.customersFromDiscardTask();
        }).start();
        logger.info("5.1.customersFromDiscardTask end");

        logger.info("6.sandMessage start");
        new Thread(()->{
            customerInfoService.sandMessage();
        }).start();
        logger.info("6.sandMessage end");

        logger.info("7.ezuc qurtz start");
        new Thread(()->{
            ezucQurtzService.syncData4Qurtz();
        }).start();
        logger.info("7.ezuc qurtz end");

        logger.info("8.ezuc qurtz start");
        new Thread(()->{
            customerInfoService.pushCustomer();
        }).start();
        logger.info("8.ezuc qurtz end");

        logger.info("ScheduledJob End!");
    }
}
