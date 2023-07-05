package com.proxy;

/**
 * @Author Liwei
 * @Date 2022/7/19 0:30
 */
public class CalculatorImpl implements Calculator {
    @Override
    public int add(int a, int b) {
        int result = a + b;
        return result;
    }

    @Override
    public int subtract(int a, int b) {
        int result = a - b;
        return result;
    }
}
