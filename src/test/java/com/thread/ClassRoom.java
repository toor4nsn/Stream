package com.thread;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Semaphore;

public class ClassRoom {
    public Semaphore sp;

    public ClassRoom(Semaphore sp) {
        this.sp = sp;
    }

    public void into() {
        // 获得许可
        try {
            sp.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ":获得许可,进入了教室...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ":释放许可,离开教室");
        // 释放许可
        sp.release();
    }


}
