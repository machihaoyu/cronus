package com.fjs.cronus.config;

import com.fjs.cronus.service.CustomerInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

@Configuration
@EnableScheduling
public class NewCustomerScheduledConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private CustomerInfoService customerInfoService;

    @Scheduled(cron = "0 0 20 * * ?")//定时任务:新注册15天之后的客户,发送短信
    public void sandMessage(){
        logger.error("定时任务 ------> 新注册客户15天发送短信");
        customerInfoService.sandMessage();
    }


}
