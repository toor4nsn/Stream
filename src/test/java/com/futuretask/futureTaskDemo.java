package com.futuretask;

import java.util.concurrent.*;

/**
 * @Author Liwei
 * @Date 2022/4/10 19:28
 */
public class futureTaskDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 单线程的线程池
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // 为了看清楚点，我把Callable提出来赋值
        Callable<String> callable = () -> {
            System.out.println(Thread.currentThread().getName() + "========>正在执行");
            Thread.sleep(3 * 1000L);
            System.out.println("3秒");
            return "success";
        };

        // 提交Callable任务
        Future<String> result = executorService.submit(callable);
        // 主线程继续往下走
        Thread.sleep(1000L);
        System.out.println("1秒");
        Thread.sleep(1000L);
        System.out.println("2秒");
        Thread.sleep(1000L);
        System.out.println("3秒");
        // 获取异步结果。
        System.out.println("result=" + result.get());
    }

}
