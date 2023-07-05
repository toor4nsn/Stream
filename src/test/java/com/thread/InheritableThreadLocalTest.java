package com.thread;

public class InheritableThreadLocalTest {
    
    public static void main(String[] args) {
//        final ThreadLocal threadLocal = new ThreadLocal();
        final ThreadLocal threadLocal = new InheritableThreadLocal();
        // 主线程
        threadLocal.set("不擅技术");


        //子线程
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();
                System.out.println("鄙人三某 ，" + threadLocal.get());
            }
        };
        t.start();
    }
}
