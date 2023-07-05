package com.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class SimpleThreadPool {

    /**
     * 任务队列
     */
    BlockingQueue<Runnable> workQueue;

    /**
     * 工作线程
     */
    List<Worker> workers = new ArrayList<>();

    /**
     * 构造器
     *
     * @param poolSize  线程数
     * @param workQueue 任务队列
     */
    SimpleThreadPool(int poolSize, BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        // 创建线程，并加入线程池
        for (int i = 0; i < poolSize; i++) {
            Worker work = new Worker();
            work.start();
            workers.add(work);
        }
    }

    /**
     * 提交任务
     *
     * @param command
     */
    void execute(Runnable command) {
        try {
            // 任务队列满了则阻塞
            workQueue.put(command);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 工作线程，负责执行任务
     */
    class Worker extends Thread {
        public void run() {
            // 循环获取任务，如果任务为空则阻塞等待
            while (true) {
                try {
                    Runnable task = workQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}