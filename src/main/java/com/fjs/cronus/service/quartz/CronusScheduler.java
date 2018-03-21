/*
package com.fjs.cronus.service.quartz;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

*/
/**
 * Created by zl on 2017/12/11
 *//*

@Service
public class CronusScheduler {

    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;

    public void scheduleJobs() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        startJob1(scheduler);
        startJob2(scheduler);
    }
    private void startJob1(Scheduler scheduler) throws SchedulerException{
        JobKey key = new JobKey("sendmess", "cronus-quartz");
        if (!scheduler.checkExists(key)) {
            JobDetail jobDetail = JobBuilder.newJob(MySchedulerJob.class).withIdentity("sendmess", "cronus-quartz").storeDurably(true).build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("sendmess", "cronus-quartz").withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, cronTrigger);
        }
    }
    private void startJob2(Scheduler scheduler) throws SchedulerException{
        JobKey key = new JobKey("sendcomm", "cronus-quartz");
        if (!scheduler.checkExists(key)) {
            JobDetail jobDetail = JobBuilder.newJob(MySchedulerJob2.class).withIdentity("sendcomm", "cronus-quartz").storeDurably(true).build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/5 * * * * ?");
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("sendcomm", "cronus-quartz").withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, cronTrigger);
        }
    }

}
*/
