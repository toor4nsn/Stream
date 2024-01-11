package com.annotation;

import org.junit.jupiter.api.Test;

import javax.sound.midi.Soundbank;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Author Liwei
 * @Date 2021/8/14 22:34
 */
public class MyAnnotationTest {
    @MyAnnotation(name = "张三",age = 18, names = {"Are", "you", "OK?"})
    public void show1(){
    }

    public void show2(){
    }

    @Test
    public void test() throws NoSuchMethodException {
        Class<MyAnnotationTest> clazz = MyAnnotationTest.class;
        Method show1 = clazz.getDeclaredMethod("show1");
        MyAnnotation annotation = show1.getAnnotation(MyAnnotation.class);
        System.out.println(annotation);
        System.out.println("--------------");
        System.out.println(annotation.age());
        System.out.println(annotation.name());
        System.out.println(Arrays.toString(annotation.names()));


    }
}
