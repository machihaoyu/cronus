package com.fjs.cronus.service.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;

/**
 * Created by zl on 2017/12/11
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CustomDetailQuartzJobBean extends QuartzJobBean {
    private String targetObject;
    private String targetMethod;
    private ApplicationContext ctx;

    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {
        try {

            Object otargetObject = ctx.getBean(targetObject);
            Method m = null;
            try {
                m = otargetObject.getClass().getMethod(targetMethod,
                        new Class[] {});
                m.invoke(otargetObject, new Object[] {});
            } catch (SecurityException e) {
                //logger.error(e);
            } catch (NoSuchMethodException e) {
               // logger.error(e);
            }

        } catch (Exception e) {
            throw new JobExecutionException(e);
        }

    }

    public void setApplicationContext(ApplicationContext applicationContext){
        this.ctx=applicationContext;
    }

    public void setTargetObject(String targetObject) {
        this.targetObject = targetObject;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }
}
