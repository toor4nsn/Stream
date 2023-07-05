package com.redission;

import com.example.App;
import com.example.config.RedissonLock;
import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@SpringBootTest(classes = App.class)
public class RedissonLockTest {

    @Autowired
    private RedissonLock redissonLock;

    @Test
    public void easyLock(){
        //模拟多个10个客户端
        for (int i=0;i<10;i++) {
            Thread thread = new Thread(new LockRunnable());
            thread.start();
        }

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class LockRunnable implements Runnable {
        @Override
        public void run() {
            redissonLock.addLock("demo");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            redissonLock.releaseLock("demo");
        }
    }


    @Autowired
    private RedissonClient redissonClient;

        @Test
        public void LimiterTest(){


            RRateLimiter rateLimiter=redissonClient.getRateLimiter("rate_limiter");
            rateLimiter.trySetRate(RateType.OVERALL, 1, 1, RateIntervalUnit.SECONDS);
            ExecutorService executorService= Executors.newFixedThreadPool(10);
            for (int i=0;i<10;i++){
                executorService.submit(()->{
                    try{
                        if (rateLimiter.tryAcquire()) {
                            System.out.println("线程"+Thread.currentThread().getId()+"进入数据区："+System.currentTimeMillis());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
        }

}