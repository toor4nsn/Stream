package com.example.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private String name;
    private Integer age;


    public void method1() {
        System.out.println("method1 方法");
    }

    public void method1(int num) {
        System.out.println("method1 方法,num:" + num);
    }

    public int method1(int num1, int num2) {
        System.out.println("method1 方法,num1:" + num1 + ",num2:" + num2);
        return num1 + num2;
    }

    private void method1(int num, String str) {
        System.out.println("method1 方法,num:" + num + ",str:" + str);
    }
}