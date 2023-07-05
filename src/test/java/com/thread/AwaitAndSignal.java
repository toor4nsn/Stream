package com.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


class AwaitAndSignal extends ReentrantLock {
    public void print(String str, Condition current, Condition nextCondition) {
        for (int i = 0; i < loopNumber; i++) {
            lock();
            try {
                current.await();
                System.out.print(str);
                nextCondition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                unlock();
            }
        }
    }

    private int loopNumber;

    public AwaitAndSignal(int loopNumber) {
        this.loopNumber = loopNumber;
    }
}