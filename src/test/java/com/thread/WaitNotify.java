package com.thread;

import org.junit.jupiter.api.Test;

/**
 * @Author Liwei
 * @Date 2022/4/17 13:02
 */
public class WaitNotify {
    private int flag;
    private int count;

    public WaitNotify() {
    }

    public WaitNotify(int flag, int count) {
        this.flag = flag;
        this.count = count;
    }

    public void print(String content,int waitFlag, int nextFlag){
        for (int i = 0; i < count; i++) {
            synchronized (this){
                while (flag !=waitFlag){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(content);
                flag = nextFlag;
                this.notifyAll();
            }
        }
    }

}
