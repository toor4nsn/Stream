package com.proxy;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author Liwei
 * @Date 2022/7/19 0:33
 */
public class DynamicProxyTest {
    @Test
    public void test(){
        //1.目标对象
        CalculatorImpl target = new CalculatorImpl();
        // 2.传入目标对象，得到增强对象
        InvocationHandler handler=  getLogInvocationHandler(target);

        //3.传入目标对象+增强代码，得到代理对象
        Calculator proxyInstance = (Calculator) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), handler);

        int add = proxyInstance.add(1, 2);
        System.out.println(add);

    }
     @Test
     public void test2(){
         CalculatorImpl target = new CalculatorImpl();

         Calculator proxyInstance = (Calculator) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), (proxy, method, args) -> {
             System.out.println(method.getName() + "方法开始执行...");
             Object result = method.invoke(target, args);
             System.out.println(result);
             System.out.println(method.getName() + "方法开始结束...");
             return result;
         });
         int res = proxyInstance.add(1, 2);
         System.out.println(res);
     }

    private InvocationHandler getLogInvocationHandler(CalculatorImpl target) {
        return new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method.getName() + "方法开始执行...");
                Object result = method.invoke(target, args);
                System.out.println(result);
                System.out.println(method.getName() + "方法开始结束...");
                return result;
            }
        };
    }
}
