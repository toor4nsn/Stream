package com.thread;

import java.util.concurrent.CountDownLatch;

public class MyRunnable2 implements Runnable {

    CountDownLatch cdl;

    public MyRunnable2(CountDownLatch cdl) {
        this.cdl = cdl;
    }

    @Override
    public void run() {
        System.out.println("B");
        // 计数器-1
        cdl.countDown();// 计数器的值为0
    }
}