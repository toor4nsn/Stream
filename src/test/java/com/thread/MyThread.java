package com.thread;

import java.util.concurrent.atomic.AtomicInteger;

public class MyThread extends Thread {

    volatile static boolean flag = false;// 主和子线程共享变量

/*    @Override
    public void run() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 把flag的值改为true
        flag = true;
        System.out.println("修改后flag的值为:"+flag);

    }*/
//    static int a = 0;
    static AtomicInteger a = new AtomicInteger();// 表示整数0

    @Override
    public void run() {
        for (int i = 0; i < 100000; i++) {
//            a++;
            a.getAndIncrement();
        }
        System.out.println("子线程执行完毕");
    }
}