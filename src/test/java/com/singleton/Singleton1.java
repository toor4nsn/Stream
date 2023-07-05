package com.singleton;

import com.example.po.Singleton;

/**
 * @Author Liwei
 * @Date 2022/6/4 22:58
 * 饿汉式（线程安全）
 */
public class Singleton1 {
    //构造器私有化
    private Singleton1() {}
    private static Singleton1 instance=new Singleton1();

    public static Singleton1 getInstance(){
        return instance;
    }
}
