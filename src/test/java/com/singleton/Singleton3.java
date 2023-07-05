package com.singleton;

import com.example.po.Singleton;

/**
 * @Author Liwei
 * @Date 2022/6/4 23:19
 * <p>
 * 懒汉式（双重校验锁）
 */
public class Singleton3 {
    private Singleton3() {
    }
    //这里共享变量 用 volatile 修改，防止指令重排序
    public static volatile Singleton3 instance;

    public static Singleton3 getInstance(){
        //第一重校验，检查实例是否存在
        if (instance ==null){
            synchronized (Singleton3.class){
                //第二重校验，检查实例是否存在，不存在才真正创建实例。
                if (instance==null){
                    instance=new Singleton3();
                }
            }
        }
        return instance;
    }
}
