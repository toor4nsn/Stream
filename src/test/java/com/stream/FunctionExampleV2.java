package com.stream;

import cn.hutool.core.io.LineHandler;

import java.util.function.Function;

public class FunctionExampleV2 {
    public static void main(String[] args) {
        // 定义一个简单的函数：接收x（值），返回x+1（值）
        Function<Integer, Integer> fSimple = x -> {
            return x + 1;
        };

        // 定义稍微复杂一点的函数：接收x（值），返回fn（函数）
        Function<Integer, Function<Integer, Integer>> fComplex_obj =
                // 外层函数：接收x（值），返回fn（函数）
                x -> {
                    // 内层函数fn：接收y（值），返回x+y+1（值）。当然，你返回y+1或其他任何结果都没问题
                    return new Function<Integer, Integer>() {
                        @Override
                        public Integer apply(Integer y) {
                            return x + y + 1;
                        }
                    };
                };

        // 把内层的fn匿名对象改写成lambda
        Function<Integer, Function<Integer, Integer>> fComplex_opti1 =
                x -> {
                    return y -> {
                        return x + y + 1;
                    };
                };

        // 再优化
        Function<Integer, Function<Integer, Integer>> fComplex_opti2 = x -> {
            return y -> x + y + 1;
        };

        // 最终态
        Function<Integer, Function<Integer, Integer>> fComplex_lambda = x -> y -> x + y + 1;

        // 解读表达式时，要正确区分局部和整体：先看括号内的函数，再看整体的函数
        Function<Integer, Function<Integer, Integer>> fComplex = x -> (y -> x + y + 1);

        // 接下来，我想制造一个更复杂的函数fHigher：接收f（函数），返回fTwice（函数）。fTwice的逻辑是调用两次f
        // 能这样写吗：f -> { return f.apply(f.apply(x)) }
        // 不，有两个问题：
        // 1.f.apply是函数调用，所以f.apply(f.apply(x))执行结果是一个值，而非函数。不符合要求。
        // 2.凭空出现了x。数学上可以这么写，但lambda表达式的入参必须标明。
        // 所以，还是别急，给自己一点时间，一步步慢慢来吧。

        // 改进：f -> { return x -> f.apply(f.apply(x)) }，整体是fHigher函数，接收一个f（函数），返回一个新函数fTwice
        Function<Function<Integer, Integer>, Function<Integer, Integer>> fHigher =
                f -> {
                    return x -> {
                        return f.apply(f.apply(x));
                    };
                };

        // 咋一看，fHigher和fComplex_opti1几乎一样，但请注意，fHigher的f是函数而非值，所以内部可以f.apply。

        // 简化到最终形态
        Function<Function<Integer, Integer>, Function<Integer, Integer>> fHigher_lambda = f -> (x -> f.apply(f.apply(x)));

        Function<Integer, Integer> fTwice = fHigher_lambda.apply(fSimple); // f是实参，fSimple是fHigher的形参
        Integer result = fTwice.apply(3);
        // 输出5，因为 addOne(addOne(3)) = 3 + 1 + 1 = 5
        System.out.println(result);



    }
}
