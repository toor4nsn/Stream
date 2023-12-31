package com.generic;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class GenericClassDemo {

    public static void main(String[] args) throws Exception {

        List<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        // 编译器会阻止
        // list.add(333);

        // 但泛型约束只存在于编译期，底层仍是Object，所以运行期可以往List存入任何类型的元素
        Method addMethod = list.getClass().getDeclaredMethod("add", Object.class);
        addMethod.invoke(list, 333);

        System.out.println(list);
        System.out.println("----------");
        // 打印输出观察是否成功存入Integer（注意用Object接收）
        for (Object obj : list) {
            System.out.println(obj);
        }
    }
}