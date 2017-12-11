package com.fjs.cronus.service.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Created by zl on 2017/12/11
 */
//@Service
public class CronusScheduler {

    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;

    public void scheduleJobs() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        startJob1(scheduler);
//        startJob2(scheduler);
    }
    private void startJob1(Scheduler scheduler) throws SchedulerException{
//        JobKey key = new JobKey("job1", "group1");
////        if (!scheduler.checkExists(key)) {
//            JobDetail jobDetail = JobBuilder.newJob(ScheduledJob.class).withIdentity("job1", "group1").build();
//            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
//            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").withSchedule(scheduleBuilder).build();
//            scheduler.scheduleJob(jobDetail, cronTrigger);
//        }
    }
//    private void startJob2(Scheduler scheduler) throws SchedulerException{
//        JobDetail jobDetail = JobBuilder.newJob(ScheduledJob2.class) .withIdentity("job2", "group1").build();
//        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
//        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("trigger2", "group1") .withSchedule(scheduleBuilder).build();
//        scheduler.scheduleJob(jobDetail,cronTrigger);
//    }

}
