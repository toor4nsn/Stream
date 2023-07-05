package com.example.service;

/**
 * @Author Liwei
 * @Date 2021/7/10 15:29
 */
public class MethodReference {
//• 消费型接口 Consumer<T>     T t -> void        例：x -> System.out.println(x)
//• 供给型接口 Supplier<T>       () -> T t            例：() -> {return 1+2;}
//• 映射型接口 Function<T, R>   T t -> R r          例：user -> user.getAge()，T是入参，R是出参
//• 断定型接口 Predicate<T>     T t -> boolean    例：user -> user.getAge()>18
    public static void main(String[] args) {
        //T t -> void
        //()-> T t;
        //T t -> R r;
        //T t -> boolean;
    }
}
