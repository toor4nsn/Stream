package com.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CustomAnnotationTest {
    public static void main(String[] args) throws Exception {
        // 获取类上的注解
        Class<CustomDemo> clazz = CustomDemo.class;
        CustomAnnotation annotationOnClass = clazz.getAnnotation(CustomAnnotation.class);
        System.out.println(annotationOnClass.getValue());

        // 获取成员变量上的注解
        Field name = clazz.getField("name");
        CustomAnnotation annotationOnField = name.getAnnotation(CustomAnnotation.class);
        System.out.println(annotationOnField.getValue());

        // 获取hello方法上的注解
        Method hello = clazz.getMethod("hello", (Class<?>[]) null);
        CustomAnnotation annotationOnMethod = hello.getAnnotation(CustomAnnotation.class);
        System.out.println(annotationOnMethod.getValue());

        // 获取defaultMethod方法上的注解
        Method defaultMethod = clazz.getMethod("defaultMethod", (Class<?>[]) null);
        CustomAnnotation annotationOnDefaultMethod = defaultMethod.getAnnotation(CustomAnnotation.class);
        System.out.println(annotationOnDefaultMethod.getValue());

    }
}