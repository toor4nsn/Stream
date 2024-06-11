package com.example.config;//#获取数据源多线程初始化
//executor:
//  common:
//    corePoolSize: 10
//    maxPoolSize: 50
//    keepAliveSecond: 300
//    queueCapacity: 1000

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@EnableAsync
@Configuration
public class Executor {
    private static final String THREAD_PRE_NAME = "commonExecutor-";

    @Value("${executor.common.corePoolSize:10}")
    private Integer corePoolSize;

    @Value("${executor.common.maxPoolSize:50}")
    private Integer maxPoolSize;

    @Value("${executor.common.keepAliveSecond:300}")
    private Integer keepAliveSecond;

    @Value("${executor.common.queueCapacity:1000}")
    private Integer queueCapacity;

    @Bean("commonExecutor")
    public ThreadPoolExecutor commonExecutor() {
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveSecond, TimeUnit.SECONDS,
                new LinkedBlockingQueue(queueCapacity), Thread::new, new ThreadPoolExecutor.CallerRunsPolicy());
    }
}