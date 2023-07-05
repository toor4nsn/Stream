package com.example.po;

/**
 * @Author Liwei
 * @Date 2022/3/26 21:01
 */
public class Singleton {
    public Singleton() {
    }
    private volatile static Singleton uniqueInstance;

    public static Singleton getUniqueInstance(){
        if (uniqueInstance == null) {
            synchronized (Singleton.class){
                if (uniqueInstance == null) {
                    uniqueInstance = new Singleton();
                }
            }
        }
        return uniqueInstance;
    }
}
