package com.generic;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author Liwei
 * @Date 2021/8/15 18:35
 */
public class Demo1 {
    public static void main(String[] args) {
//        List<Integer> integerList = new ArrayList<>();
//        print(integerList);
//        List<String> stringList = new ArrayList<>();
//        print(stringList);


        // 直接往String[]存Integer会编译错误
        String[] strings = new String[3];
        strings[0] = "a";
        strings[1] = "b";
        //strings[2] = 100; // COMPILE ERROR!

        // 但数组允许String[]赋值给Object[]
        Object[] objects = strings;
        // 这样就能通过编译了，但运行期会抛异常：ArrayStoreException
        //->数组允许String[]赋值给Object[]确实算是个缺陷
        //objects[2] = 100;  //java.lang.ArrayStoreException

        System.out.println("--------------");
        Object[] objArray = {100, 100L, new BigDecimal("0.0001")};
        Number intValue = (Number) objArray[0];
        Number longValue = (Number) objArray[1];
        Number decimalValue = (Number) objArray[2];
        System.out.println(intValue + "---" + longValue + "---" + decimalValue);
    }

    public static void print(List<? extends Number> list) {
        // 打印...
        System.out.println(list);
    }

    @Test
    public void test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Integer> list = new ArrayList<>();

        list.add(12);
//这里直接添加会报错
//        list.add("a");
        Class<? extends List> clazz = list.getClass();
        Method add = clazz.getDeclaredMethod("add", Object.class);
//但是通过反射添加，是可以的
        add.invoke(list, "kl");

        System.out.println(list);
    }

    @Test
    public void testt() throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        Class<?> aClass = Class.forName("com.example.po.Person");
        Object o = getObject(aClass);

        System.out.println(o);

    }

    public <T> T getObject(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        T t = clazz.newInstance();
        return t;
    }

}
