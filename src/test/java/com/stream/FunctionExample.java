package com.stream;
import java.util.function.*;

public class FunctionExample {
    public static void main(String[] args) {
        // 创建一个高阶函数：接受一个函数 f，返回一个新的函数，该新函数将 f 应用两次
        Function<Function<Integer, Integer>, Function<Integer, Integer>> applyTwice =
                new Function<Function<Integer, Integer>, Function<Integer, Integer>>() {
                    @Override
                    public Function<Integer, Integer> apply(Function<Integer, Integer> f) {
                        // apply是一个函数，它的返回值也是一个函数apply（不过外面包了一层Function对象）
                        return new Function<Integer, Integer>() {
                            @Override
                            public Integer apply(Integer x) {
                                return f.apply(f.apply(x));
                            }
                        };
                    }
                };

        // 定义一个简单的函数
        Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer x) {
                return x + 1;
            }
        };

        // 把简单函数addOne传入高阶函数applyTwice，得到新函数addTwo
        Function<Integer, Integer> addTwo = applyTwice.apply(addOne);

        // 传入3，调用addTwo
        System.out.println(addTwo.apply(3));  // 输出 5，因为 addOne(addOne(3)) = 3 + 1 + 1 = 5
    }
}