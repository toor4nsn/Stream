package com.annotation;

/**
 * @Author Liwei
 * @Date 2021/8/14 16:45
 *
 */

import org.yaml.snakeyaml.events.Event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取注解并操作
 */
public class MyJunitFrameWork {
    public static void main(String[] args) throws Exception{
        // 1.先找到测试类的字节码：EmployeeDAOTest
        Class<EmployeeDAOTest> clazz = EmployeeDAOTest.class;
        Object obj = clazz.newInstance();
        // 2.获取EmployeeDAOTest类中所有的公共方法
        Method[] methods = clazz.getDeclaredMethods();
        // 3.迭代出每一个Method对象，判断哪些方法上使用了@MyBefore/@MyAfter/@MyTest注解
        List<Method> myBeforeList = new ArrayList<>();
        List<Method> myAfterList = new ArrayList<>();
        List<Method> myTestList = new ArrayList<>();

        for (Method m : methods) {
            if (m.isAnnotationPresent(MyBefore.class)){
                myBeforeList.add(m);
            }else if(m.isAnnotationPresent(MyAfter.class)){
                myAfterList.add(m);
            }else if (m.isAnnotationPresent(MyTest.class)){
                myTestList.add(m);
            }
        }

        // 执行方法测试
        for (Method t : myTestList) {
            for (Method b : myBeforeList) {
                b.invoke(obj);
            }
            t.invoke(obj);
            for (Method a : myAfterList) {
                a.invoke(obj);
            }
        }
    }
}
