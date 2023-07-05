package com.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SimpleThreadPoolTest {
    
    public static void main(String[] args) {
        
        SimpleThreadPool simpleThreadPool = new SimpleThreadPool(2, new ArrayBlockingQueue<Runnable>(2));
        
        simpleThreadPool.execute(() -> {
            System.out.println("第1个任务开始");
            sleep(3);
            System.out.println("第1个任务结束");
        });
        simpleThreadPool.execute(() -> {
            System.out.println("第2个任务开始");
            sleep(4);
            System.out.println("第2个任务结束");
        });
        simpleThreadPool.execute(() -> {
            System.out.println("第3个任务开始");
            sleep(5);
            System.out.println("第3个任务结束");
        });
    }


    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}