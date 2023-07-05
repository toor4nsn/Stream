package com.thread;

import java.util.concurrent.CountDownLatch;

public class MyRunnable1 implements Runnable {
    CountDownLatch cdl;

    public MyRunnable1(CountDownLatch cdl) {
        this.cdl = cdl;
    }

    @Override
    public void run() {
        System.out.println("A");
        // 暂停,等待线程2执行打印B,执行完回到这里来执行打印C
        try {
            cdl.await();    // 让当前线程等待
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("C");// 打印C之前一定要去执行打印B
    }
}