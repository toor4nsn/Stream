package com.futuretask;

import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class AsyncAndWaitTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 第一次看到FutureTask时，相信大家会震惊：啥玩意，怎么把Callable往FutureTask里塞呢？！
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(Thread.currentThread().getName() + "========>正在执行");
                try {
                    Thread.sleep(3 * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "success";
            }
        });
        

        // 看到这，你再次震惊：啥玩意，怎么又把FutureTask塞到Thread里了呢？！
        new Thread(futureTask).start();
        System.out.println(Thread.currentThread().getName() + "========>启动任务");


        // 可以获取异步结果（会阻塞3秒）
        String result = futureTask.get();
        System.out.println("任务执行结束，result====>" + result);
    }

}