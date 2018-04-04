package com.fjs.cronus.config;

import com.fjs.cronus.service.App.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

@Configuration
@EnableScheduling
public class AppCountScheduledConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private AppService appService;

    @Scheduled(cron = "0 0/5* * * ?") // 每5分钟执行一次
    public void getToken() {
        logger.info("getToken定时任务启动");
        appService.getReceiveAndKeepCountRedis();
    }

}
