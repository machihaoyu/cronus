package com.fjs.cronus.service.quartz;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/3 0003.
 */
@Configuration
@Component // 此注解必加
@EnableScheduling // 此注解必加
public class ScheduledJob {

    private SimpleDateFormat dateFormat() {
        return new SimpleDateFormat("HH:mm:ss");
    }

    public void sayHello()  {
        System.out.println("AAAA: The time is now " + dateFormat().format(new Date()));
    }


    private static final Logger logger = LoggerFactory.getLogger(ScheduledJob.class);

    public void examineVideo() {

       logger.info("Examine Start!");

        System.out.println("我爱王倩");

        logger.info("Examine End!");
    }
}
