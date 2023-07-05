package com.completableFuture;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @Author Liwei
 * @Date 2022/4/9 18:23
 */
public class CompletableFutureDemo {
    @Test
    public void testCallBack() throws InterruptedException, ExecutionException {
        // 提交一个任务，返回CompletableFuture
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                System.out.println("=============>异步线程开始...");
                System.out.println("=============>异步线程为：" + Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("=============>异步线程结束...");
                return "supplierResult";
            }
        });

        // 阻塞获取结果
        System.out.println("异步结果是:" + completableFuture.get());
        System.out.println("main结束");
    }
    @Test
    public void testCallBack2() throws InterruptedException, ExecutionException {
        // 提交一个任务，返回CompletableFuture（注意，并不是把CompletableFuture提交到线程池，它没有实现Runnable）
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                System.out.println("=============>异步线程开始...");
                System.out.println("=============>异步线程为：" + Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("=============>异步线程结束...");
                return "supplierResult";
            }
        });

        // 异步回调：上面的Supplier#get()返回结果后，异步线程会回调BiConsumer#accept()
        completableFuture.whenComplete(new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String s, Throwable throwable) {
                System.out.println("=============>异步任务结束回调...");
                System.out.println("=============>回调线程为：" + Thread.currentThread().getName());
            }
        });

        // CompletableFuture的异步线程是守护线程，一旦main结束就没了，为了看到打印结果，需要让main休眠一会儿
        System.out.println("main结束");
        TimeUnit.SECONDS.sleep(15);
    }
}
