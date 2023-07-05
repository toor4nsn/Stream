package com.thread;

public class MeetingRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("会议开始,会议的内容是...");
    }
}