package com.thread;

/**
 * @Author Liwei
 * @Date 2022/4/5 11:33
 */
public class Worker implements Runnable{

    public void begin(){
        new Thread(this).start();
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "执行Worker#run() begin");
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "执行Worker#run() end");
    }
}
