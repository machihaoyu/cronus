package com.fjs.cronus.config;

import com.fjs.cronus.service.quartz.CustomDetailQuartzJobBean;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by zl on 2017/12/11
 */
@Configuration
public class SchedulerConfig {

    @Value("${org.quartz.scheduler.instanceName}")
    private String instanceName;

    @Value("${org.quartz.scheduler.instanceId}")
    private String instanceId;

    @Value("${org.quartz.threadPool.class}")
    private String threadPoolClass;

    @Value("${org.quartz.threadPool.threadCount}")
    private String threadCount;

    @Value("${org.quartz.threadPool.threadPriority}")
    private String threadPriority;

    @Value("${org.quartz.jobStore.misfireThreshold}")
    private String misfireThreshold;

    @Value("${org.quartz.jobStore.class}")
    private String jobStoreClass;

    @Value("${org.quartz.jobStore.tablePrefix}")
    private String tablePrefix;

    @Value("${org.quartz.jobStore.dataSource}")
    private String dataSourceName;

    @Value("${org.quartz.jobStore.isClustered}")
    private String isClustered;

    @Value("${org.quartz.jobStore.clusterCheckinInterval}")
    private String clusterCheckinInterval;

    @Value("${org.quartz.dataSource.maxConnections}")
    private String maxConnections;

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driver_class;

    @Bean(name = "jobDetail")
    public JobDetailFactoryBean detailFactoryBean() {// ScheduleTask为需要执行的任务
        JobDetailFactoryBean jobDetail = new JobDetailFactoryBean();
        /*
         *  是否并发执行
         *  例如每5s执行一次任务，但是当前任务还没有执行完，就已经过了5s了，
         *  如果此处为true，则下一个任务会执行，如果此处为false，则下一个任务会等待上一个任务执行完后，再开始执行
         */
        jobDetail.setJobClass(CustomDetailQuartzJobBean.class);
        jobDetail.setDurability(true);

        jobDetail.setName("examineVideo");// 设置任务的名字
        jobDetail.setGroup("d-quartz");// 设置任务的分组，这些属性都可以存储在数据库中，在多任务的时候使用

        Map<String, String> jobDataMap = new HashMap<String,String>();
        jobDataMap.put("targetObject","scheduledJob");
        jobDataMap.put("targetMethod", "examineVideo");

        jobDetail.setJobDataAsMap(jobDataMap);

        return jobDetail;
    }

    @Bean(name = "jobTrigger")
    public CronTriggerFactoryBean cronJobTrigger(JobDetailFactoryBean jobDetail) {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail(jobDetail.getObject());
        tigger.setCronExpression("0/5 * * * * ?");// 初始时的cron表达式//每5秒执行一次
        tigger.setName("examineVideo");// trigger的name
        return tigger;

    }

    @Primary
    @Bean(name = "scheduler")
    public SchedulerFactoryBean schedulerFactoryBean(Trigger cronJobTrigger) throws IOException {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setStartupDelay(20);

        schedulerFactoryBean.setQuartzProperties(quartzProperties());
        schedulerFactoryBean.setTriggers(cronJobTrigger);
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
        return schedulerFactoryBean;
    }

      private Properties quartzProperties() throws IOException {

          Properties properties = new Properties();

          properties.setProperty("org.quartz.scheduler.instanceName",instanceName);
          properties.setProperty("org.quartz.scheduler.instanceId",instanceId);
          properties.setProperty("org.quartz.threadPool.class",threadPoolClass);
          properties.setProperty("org.quartz.threadPool.threadCount",threadCount);
          properties.setProperty("org.quartz.threadPool.threadPriority",threadPriority);
          properties.setProperty("org.quartz.jobStore.misfireThreshold",misfireThreshold);
          properties.setProperty("org.quartz.jobStore.class",jobStoreClass);
          properties.setProperty("org.quartz.jobStore.tablePrefix",tablePrefix);
          properties.setProperty("org.quartz.jobStore.dataSource",dataSourceName);
          properties.setProperty("org.quartz.jobStore.isClustered",isClustered);
          properties.setProperty("org.quartz.jobStore.clusterCheckinInterval",clusterCheckinInterval);
          properties.setProperty("org.quartz.dataSource.myDS.driver",driver_class);
          properties.setProperty("org.quartz.dataSource.myDS.URL",url);
          properties.setProperty("org.quartz.dataSource.myDS.user",username);
          properties.setProperty("org.quartz.dataSource.myDS.password",password);
          properties.setProperty("org.quartz.dataSource.myDS.maxConnections",maxConnections);

          return properties;
      }
}
