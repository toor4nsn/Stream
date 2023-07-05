package com.thread;

import com.google.errorprone.annotations.Var;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author Liwei
 * @Date 2022/4/5 20:27
 */
public class ConditionQueue<T> {
    // 容器，用来装东西
    private final LinkedList<T> queue = new LinkedList<>();

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition producerCondition = lock.newCondition();
    private final Condition consumerCondition = lock.newCondition();

    public void put(T resource) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() >= 1) {
                System.out.println("生产者：队列满了！无法插入！");
                //生产者阻塞
                producerCondition.await();
            }
            System.out.println("生产者插入：" + resource);
            queue.addFirst(resource);
            //生产完毕，唤醒消费者
            consumerCondition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void take() throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() <= 0) {
                // 队列空了，不能再取东西，轮询等待生产者插入数据
                System.out.println("消费者：队列为空，无法取出...");
                // 消费者阻塞
                consumerCondition.await();
            }
            System.out.println("消费者：取出消息!!!");
            queue.removeLast();
            // 消费完毕，唤醒生产者
            producerCondition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
