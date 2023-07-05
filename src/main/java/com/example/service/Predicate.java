package com.example.service;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 新建Predicate接口
 *
 * @param <T>
 */
@FunctionalInterface
interface Predicate<T> {
    /**
     * 定义了一个test()方法，传入任意对象，返回true or false，具体判断逻辑由子类实现
     *
     * @param t
     * @return
     */
    boolean test(T t);
}


/**
 * Predicate接口的实现类，泛型规定只处理Person
 */
class PredicateImpl implements Predicate<Person> {

    /**
     * 判断逻辑是：传入的person是否age>18，是就返回true
     *
     * @param person
     * @return
     */
    @Override
    public boolean test(Person person) {
        return person.getAge() > 18;
    }
}


@Data
@AllArgsConstructor
class Person {
    private String name;
    private Integer age;
}