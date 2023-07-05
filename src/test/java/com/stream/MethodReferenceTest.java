package com.stream;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MethodReferenceTest {

    private static final List<Person> list;

    static {
        list = new ArrayList<>();
        list.add(new Person(19));
        list.add(new Person(18));
        list.add(new Person(20));
    }

    public static void main(String[] args) {
        System.out.println(list);
        // sort()方法是List本身就有的，主要用来排序
        list.sort((p1, p2) -> p1.getAge() - p2.getAge());
        System.out.println(list);
        System.out.println("----------------");

        // 改动2：既然Person内部有个逻辑一样的方法，就用它来替换Lambda
        list.sort(Person::compare);
        System.out.println(list);


        System.out.println("----------------");
        list.sort(Comparator.comparingInt(Person::getAge));
        System.out.println(list);
    }


    @Data
    @AllArgsConstructor
    static class Person {
        private Integer age;
        // 改动1：新增一个方法，逻辑和之前案例的Lambda表达式相同
        public static int compare(Person p1,Person p2){
            return p1.getAge()-p2.getAge();
        }
    }

}