package com.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadPoolDebug {
    public static void main(String[] args) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1,                           // 核心线程数1
                2,                           // 最大线程数2
                1,                           // 存活时间设为1小时，避免测试期间线程被回收
                TimeUnit.HOURS,
                new ArrayBlockingQueue<>(1)  // 阻塞队列长度为1
        );

        threadPoolExecutor.execute(() -> {
            log.info("{}:执行第1个任务...", Thread.currentThread().getName());
            sleep(100);
        });

        sleep(1);

        threadPoolExecutor.execute(() -> {
            log.info("{}:执行第2个任务...", Thread.currentThread().getName());
            sleep(100);

        });

        sleep(1);

        threadPoolExecutor.execute(() -> {
            log.info("{}:执行第3个任务...", Thread.currentThread().getName());
            sleep(100);
        });

        sleep(1);

        threadPoolExecutor.execute(() -> {
            log.info("{}:执行第4个任务...", Thread.currentThread().getName());
            sleep(100);
        });

        sleep(1);

        log.info("main结束");
    }

    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}