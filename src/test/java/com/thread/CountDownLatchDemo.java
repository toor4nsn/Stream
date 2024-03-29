package com.thread;

import com.example.App;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

/**
 * @Author Liwei
 * @Date 2022/4/19 0:55
 */
@Slf4j
public class CountDownLatchDemo {
    @Test
    public void test(){
        CountDownLatch countDownLatch = new CountDownLatch(3);
        new Thread(()->{
            log.debug("t1 start");
            try {
                sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("t1 end");
            countDownLatch.countDown();
        },"t1").start();
        new Thread(()->{
            log.debug("t2 start");
            try {
                sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("t2 end");
            countDownLatch.countDown();
        },"t2").start();

        new Thread(()->{
            log.debug("t3 start");
            try {
                sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("t3 end");
            countDownLatch.countDown();
        },"t3").start();

        log.debug("main Thread start");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("main Thread end");
    }

    @Test
    public void method2() {
        CountDownLatch countDownLatch = new CountDownLatch(3);

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(() -> {
            log.info("t1 start ...");

            try {
                sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("t1 end ...{}", countDownLatch.getCount());
            countDownLatch.countDown();
        });

        executorService.submit(() -> {
            log.info("t2 start ...");

            try {
                sleep(3L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("t2 end ...{}", countDownLatch.getCount());
            countDownLatch.countDown();
        });

        executorService.submit(() -> {
            log.info("t3 start ...");

            try {
                sleep(5L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("t3 end ...{}", countDownLatch.getCount());
            countDownLatch.countDown();
        });

        executorService.submit(() -> {
            log.info("Summary main wait ...");
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("Summary main wait end ...");
            executorService.shutdown();
        });
    }

    @Test
    public void method3() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        Random random = new Random();
        String[] all=new String[10];

        for (int j = 0; j < 10; j++) {
            int finalJ = j;
            executorService.submit(()->{
                for (int i = 0; i <= 100; i++) {
                    try {
                        Thread.sleep(random.nextInt(10));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    all[finalJ]=i+"%";
                    log.debug("\r"+ Arrays.toString(all));
                }
                countDownLatch.countDown();
            });
        }


        countDownLatch.await();
        log.info("game begin");

    }


    @Test
    public void xjidhe() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);

        Thread 大乔 = new Thread(countDownLatch::countDown);
        Thread 兰陵王 = new Thread(countDownLatch::countDown);
        Thread 安其拉 = new Thread(countDownLatch::countDown);
        Thread 哪吒 = new Thread(countDownLatch::countDown);
        Thread 铠 = new Thread(() -> {
            try {
                // 稍等，上个卫生间，马上到...
                Thread.sleep(5500);
                countDownLatch.countDown();
            } catch (InterruptedException ignored) {}
        });

        大乔.start();
        兰陵王.start();
        安其拉.start();
        哪吒.start();
        铠.start();
        countDownLatch.await();
        System.out.println("所有玩家已经就位！");
    }

    @Test
    public void anotherTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Thread 大乔 = new Thread(() -> waitToFight(countDownLatch));
        Thread 兰陵王 = new Thread(() -> waitToFight(countDownLatch));
        Thread 安其拉 = new Thread(() -> waitToFight(countDownLatch));
        Thread 哪吒 = new Thread(() -> waitToFight(countDownLatch));
        Thread 铠 = new Thread(() -> waitToFight(countDownLatch));

        大乔.start();
        兰陵王.start();
        安其拉.start();
        哪吒.start();
        铠.start();

        Thread.sleep(1000);
        countDownLatch.countDown();
        System.out.println("敌方还有5秒达到战场，全军出击！");
    }
    private static void waitToFight(CountDownLatch countDownLatch) {
        try {
            countDownLatch.await(); // 在此等待信号再继续
            System.out.println("收到，发起进攻！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
