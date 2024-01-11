package com.thread;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

/**
 * @Author Liwei
 * @Date 2022/4/5 20:27
 */
public class WaitNotifyQueue<T> {
    // 容器，用来装东西
    private final LinkedList<T> queue = new LinkedList<>();

    public synchronized void put(T resource) throws InterruptedException {
        while (queue.size() >= 1) {
            System.out.println("生产者：队列满了！无法插入！");
            this.wait();
        }
        System.out.println("生产者插入：" + resource);
        queue.addFirst(resource);
        this.notifyAll();
    }
    public synchronized void take() throws InterruptedException {
        while (queue.size() <= 0) {
            // 队列空了，不能再取东西，轮询等待生产者插入数据
            System.out.println("消费者：队列为空，无法取出...");
            this.wait();
        }
        System.out.println("消费者：取出消息!!!");
        queue.removeLast();
        this.notifyAll();
    }

    @Test
    public void test() {
        // 队列
        WaitNotifyQueue<String> queue = new WaitNotifyQueue<>();

        // 生产者
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        queue.put("消息" + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        // 消费者
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        queue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
