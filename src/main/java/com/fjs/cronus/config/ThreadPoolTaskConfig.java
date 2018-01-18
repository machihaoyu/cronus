package com.fjs.cronus.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by msi on 2017/1/18.
 */
@Configuration
@EnableAsync
public class ThreadPoolTaskConfig implements AsyncConfigurer {

    @Value("${threadPool.corePoolSize}")
    private int corePoolSize ;//设置核心线程数量

    @Value("${threadPool.maxPoolSize}")
    private int maxPoolSize ;//设置最大线程数

    @Value("${threadPool.queueCapacity}")
    private int queueCapacity ;//设置阻塞队列

    @Value("${threadPool.keepAlive}")
    private int keepAlive;//设置线程的存活时间

    /**
     * 默认线程池暂时不使用 使用自定义
     * @return
     */
    @Override
    public Executor getAsyncExecutor() {
        return null;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }


    /**
     * 使用自定义线程池发送短信或者图文识别接口
     * @return
     */
    @Bean
    public Executor mineAsyncPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAlive);
        executor.setThreadNamePrefix("CronusExecutor-");
        //拒绝策略采取默认
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
