package com.example.service;

/**
 * @Author Liwei
 * @Date 2021/7/10 17:47
 */
public interface MyInterface {
    //传统接口的抽象方法，无方法体
    public abstract void eat();
    //Java8 接口静态方法
    static void sing(){
        System.out.println("唱歌！");
    }
    //Java8 接口默认方法
    default void dance(){
        System.out.println("跳舞！");
    }
}
