package com.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

@Slf4j
public class TestDemo {
    public static void main(String[] args) throws InterruptedException {
        /*
            多线程的安全性问题-可见性:
                一个线程没有看见另一个线程对共享变量的修改
         */
/*        // 创建子线程并启动
        MyThread mt = new MyThread();
        mt.start();

        // 主线程
        while (true){
            if (MyThread.flag == true){
                System.out.println("死循环结束");
                break;
            }
        }*/
        /*
            按照分析结果应该是: 子线程把共享变量flag改为true,然后主线程的死循环就可以结束
            实际结果是: 子线程把共享变量flag改为true,但主线程依然是死循环
            为什么?
                其实原因就是子线程对共享变量flag修改后的值,对于主线程是不可见的
         */

        new MyThread().start();

        for (int i = 0; i < 100000; i++) {
//            MyThread.a++;
            MyThread.a.getAndIncrement();
        }

        // 为了保证子线程和主线程都执行完毕
        sleep(5000);

        // 打印最终共享变量a的值(子线程,主线程对a的操作都执行完毕了)
        System.out.println("最终:" + MyThread.a);
    }

    @Test
    public void testSemaphore() {
        // 需求:多线程模拟5个人进入教室,但教室只允许3个人在里面
        // 创建教室对象
        Semaphore sp = new Semaphore(3);
        ClassRoom cr = new ClassRoom(sp);

        new Thread(new Runnable() {
            @Override
            public void run() {
                cr.into();
            }
        }, "同学1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                cr.into();
            }
        }, "同学2").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                cr.into();
            }
        }, "同学3").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                cr.into();
            }
        }, "同学4").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                cr.into();
            }
        }, "同学5").start();
    }

    @Test
    public void testCountDownLatch() {
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(new MyRunnable1(latch)).start();
        new Thread(new MyRunnable2(latch)).start();
    }

    @Test
    public void testCyclicBarrier() {
        CyclicBarrier barrier = new CyclicBarrier(5, new MeetingRunnable());
        EmployeeRunnable employeeRunnable = new EmployeeRunnable(barrier);
        new Thread(employeeRunnable, "employee1").start();
        new Thread(employeeRunnable, "employee2").start();
        new Thread(employeeRunnable, "employee3").start();
        new Thread(employeeRunnable, "employee4").start();
        new Thread(employeeRunnable, "employee5").start();
    }

    @Test
    public void testExecutor() throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> submit = executor.submit(() -> {
            sleep(5000L);
            return "Nokia";
        });

        String s = submit.get(5, TimeUnit.SECONDS);
        System.out.println(s);
    }

    @Test
    public void testCopyOnWriteArrayList() throws InterruptedException {
        MyRunable myRunable = new MyRunable();
        new Thread(myRunable).start();
        new Thread(myRunable).start();
        sleep(1000);

        System.out.println("最终集合的长度：" + myRunable.list.size());
    }

    @Test
    public void testWorker() throws InterruptedException {
        Worker worker = new Worker();
        worker.begin();
        sleep(4000L);
    }

    @Test
    public void testssihx() throws InterruptedException {
        int i = false ? 1 : 10;
        System.out.println(i);
    }

    @Test
    public void testQueue() {
        // 队列
//        WaitNotifyQueue<String> queue = new WaitNotifyQueue<>();
//        WhileQueue<String> queue = new WhileQueue<>();
        ConditionQueue<String> queue = new ConditionQueue<>();
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

    @Test
    public void orderControl() {
        //first print T2
        //second print T1
        Object lock = new Object();
        AtomicBoolean hasT2Print = new AtomicBoolean(false);
        new Thread(() -> {
            synchronized (lock) {
                while (!hasT2Print.get()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("print:1");
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (lock) {
                hasT2Print.set(true);
                System.out.println("print:2");
                lock.notifyAll();
            }
        }, "t2").start();

    }

    @Test
    public void orderControl2() {
        //first print T2
        //second print T1
        Thread t1 = new Thread(() -> {
            LockSupport.park();
            System.out.println("t1");
        });
        t1.start();
        new Thread(() -> {
            System.out.println("t2");
            LockSupport.unpark(t1);
        }).start();
    }


    @Test
    public void test() {
        WaitNotify w = new WaitNotify(1, 5);
/*        new Thread(()->{w.print("a",1,2);}).start();
        new Thread(()->{w.print("b",2,3);}).start();
        new Thread(()->{w.print("c",3,1);}).start();*/


        AwaitAndSignal lock = new AwaitAndSignal(5);
        Condition a = lock.newCondition();
        Condition b = lock.newCondition();
        Condition c = lock.newCondition();
        new Thread(() -> {
            lock.print("a", a, b);
        }).start();
        new Thread(() -> {
            lock.print("b", b, c);
        }).start();
        new Thread(() -> {
            lock.print("c", c, a);
        }).start();

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lock.lock();//mian Thread get lock
        try {
            a.signal();//notify a
        } finally {
            lock.unlock();
        }

    }

    @Test
    public void testSemaphore2() {
        // 1. 创建 semaphore 对象
        Semaphore semaphore = new Semaphore(3);

        // 2. 10个线程同时运行
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    log.debug("running...");
                    sleep(1);
                    log.debug("end...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }).start();
        }

    }

    @Test
    public void testCyclicBarrier2(){
        //线程数和cyclicBarrier的parties保证一致
        ExecutorService service = Executors.newFixedThreadPool(2);
        CyclicBarrier barrier = new CyclicBarrier(2, () -> {
            log.info("task1,task2 fanish");
        });
        for (int i = 0; i < 2; i++) {
            service.submit(()->{
                log.info("task1 begin...");
                try {
                    Thread.sleep(1);
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
            service.submit(()->{
                log.info("task2 begin...");
                try {
                    Thread.sleep(5);
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Test
    public void  futureTaskDemo() throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(() -> "Callable......");
        new Thread(futureTask).start();

        String result=futureTask.get();
        System.out.println(result);
    }
    //阻塞线程直接打断 会抛出InterruptedException
    @Test
    public void interruptDemo() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                sleep(80000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "T1");
        t1.start();
        //t1 execute first
        Thread.sleep(1000);
        log.info("interrupt....");
        t1.interrupt();
    }
    //正常线程，只是设置标记为 true
    @Test
    public void interruptDemo2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                boolean flag = Thread.currentThread().isInterrupted();
                if (flag) {
                    log.debug("interrupt......break");
                    break;
                }
            }
        }, "t1");
        t1.start();
        //let t1 execute first
        //main thread sleep
        Thread.sleep(1000);
        log.debug("interrupt");
        t1.interrupt();//just set interrupt flag is true
    }

    @Test
    public void joinDemo(){
        JoinExample joinExample = new JoinExample();
        joinExample.test();

    }


    class MyRunable implements Runnable {
        //        List<Integer> list = new ArrayList<>();
        List<Integer> list = new CopyOnWriteArrayList<>();

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                list.add(i);
            }
            System.out.println("添加完毕！");
        }
    }
}