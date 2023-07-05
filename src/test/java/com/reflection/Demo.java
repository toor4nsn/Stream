package com.reflection;

import com.example.po.Person;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author Liwei
 * @Date 2021/8/14 0:07
 */
public class Demo {
    @Test
    public void test01() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Person> c = Person.class;
        Method m1 = c.getDeclaredMethod("method1");
        System.out.println(m1);
        Constructor<Person> con1 = c.getDeclaredConstructor();
        Person p1 = con1.newInstance();
        m1.invoke(p1);

        System.out.println("##################");
        Method[] methods = c.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println(m);
        }
        System.out.println("##################");
        Method m4 = c.getDeclaredMethod("method1", int.class, String.class);
        Constructor<Person> con4 = c.getDeclaredConstructor(String.class, Integer.class);
        System.out.println(con4);
        System.out.println(p1);

        m4.setAccessible(true);
        m4.invoke(p1,9527,"toor4nsn");
    }

    @Test
    public void annotationTest() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<TestDemo> clazz = TestDemo.class;
        Method[] methods = clazz.getDeclaredMethods();
        Constructor<TestDemo> constructor = clazz.getDeclaredConstructor();
        TestDemo ins = constructor.newInstance();
        for (Method m : methods) {
            boolean b = m.isAnnotationPresent(MyTest.class);
            if (b){
                m.invoke(ins);
            }
        }
    }
}
