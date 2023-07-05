package com.futuretask;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.LockSupport;

/**
 * @Author Liwei
 * @Date 2022/4/5 11:56
 */
public class AsyncAndWaitTest {
    @Test
    public void testAsync() throws ExecutionException, InterruptedException {
        //callable<>
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName() + "========>正在执行");
            try {
                Thread.sleep(3 * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "success";
        });

        //runable<>
        FutureTask<String> runableTask = new FutureTask<>(() -> System.out.println(Thread.currentThread().getName() + "======>正在执行！"), "success");
        new Thread(futureTask).start();
        System.out.println(Thread.currentThread().getName() + "========>启动任务");
        // 可以获取异步结果（会阻塞3秒）
        String result = futureTask.get();
//        String result = runableTask.get();
        System.out.println("任务执行结束，result====>" + result);
    }
    @Test
    public void testPark() throws InterruptedException {
        // 存储线程
        List<Thread> threadList = new ArrayList<>();

        // 创建5个线程
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {
                System.out.println("我是" + Thread.currentThread().getName() + ", 我开始工作了~");

                LockSupport.park(this);

                System.out.println("我是" + Thread.currentThread().getName() + ", 我又活过来了~");
            });
            thread.start();
            threadList.add(thread);
        }

        Thread.sleep(3 * 1000L);
        System.out.println("====== 所有线程都阻塞了，3秒后全部恢复了 ======");

        // unPark()所有线程
        for (Thread thread : threadList) {
            LockSupport.unpark(thread);
        }

        // 等所有线程执行完毕
        Thread.sleep(3 * 1000L);
    }
}
