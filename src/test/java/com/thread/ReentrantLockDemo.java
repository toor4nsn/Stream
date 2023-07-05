package com.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

/**
 * @Author Liwei
 * @Date 2022/4/17 0:27
 */
@Slf4j
public class ReentrantLockDemo {
    @Test
    public void lockInterruptibly() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Thread t1 = new Thread(() -> {

            log.debug("启动...");
            try {
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("等锁的过程中被打断");
                return;
            }
            try {
                log.debug("获得了锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock();
        log.debug("获得了锁");
        t1.start();
        try {
            sleep(1000);
            t1.interrupt();
            log.debug("执行打断");
        } finally {
            lock.unlock();
        }

    }

    @Test
    public void tryLock(){
        ReentrantLock lock = new ReentrantLock();
        Thread t1 = new Thread(() -> {
            // 未设置等待时间，一旦获取失败，直接返回false
            if(!lock.tryLock()) {
                System.out.println("获取失败");
                // 获取失败，不再向下执行，返回
                return;
            }
            System.out.println("得到了锁");
            lock.unlock();
        });

//        t1.start();
        lock.lock();
        try{
            t1.start();
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void tryLock2() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Thread t1 = new Thread(() -> {
            log.debug("启动...");
            try {
                if (!lock.tryLock(1, TimeUnit.SECONDS)) {
                    log.debug("获取等待1s 后失败，返回");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                log.debug("获得了锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock();
        log.debug("获得了锁");

        t1.start();
        try {
            Thread.sleep(5000L);//main持续5s
        } finally {
            lock.unlock();
        }

    }


}
