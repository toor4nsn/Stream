package com.completableFuture;

import com.example.App;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest(classes = App.class)
public class AsyncNotifyTest {

    @Test
    public void testAsyncNotify() throws InterruptedException {

        long start = System.currentTimeMillis();

        // 投递简历，插入投递记录
        TimeUnit.SECONDS.sleep(2);
        log.info("插入投递记录完毕...");

        //CompletableFuture.runAsync(Runnable task) 是Java并发编程中用于异步执行任务并返回一个CompletableFuture<Void>实例的方法，它是CompletableFuture类的一部分，
        // 该类位于java.util.concurrent包下。
        // 这个方法非常适合用于那些没有返回结果的异步操作。

        // 异步 发送短信通知HR，并留存发送记录
        CompletableFuture.runAsync(() -> {
            try {
                notifyHR("bravo1988", "叉车师傅");
                writeMsg("bravo1988", "叉车师傅");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        log.info("耗时:{}毫秒", System.currentTimeMillis() - start);


        // 为了观察到异步线程里的打印信息，主线程sleep一会儿
        TimeUnit.SECONDS.sleep(3);
    }

    public void notifyHR(String username, String jobName) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        log.info("【发送消息】HR你好，用户:{}, 投递你的岗位:{}", username, jobName);
    }

    public void writeMsg(String username, String jobName) {
        // 留存消息发送记录
        log.info("【保存消息】保存到数据库, 用户:{}, 岗位:{}", username, jobName);
    }

}