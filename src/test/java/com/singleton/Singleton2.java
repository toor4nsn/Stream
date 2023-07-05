package com.singleton;

/**
 * @Author Liwei
 * @Date 2022/6/4 23:04
 * 懒汉式（线程不安全）
 */
public class Singleton2 {
    private Singleton2() {
    }

    private static Singleton2 instance;

    public /*synchronized*/ static Singleton2 getInstance() {

        if (instance == null) {
            instance = new Singleton2();
        }
        return instance;
    }
}
