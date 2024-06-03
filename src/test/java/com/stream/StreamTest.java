package com.stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class StreamTest {

    private static List<Person> list;

    static {
        list = new ArrayList<>();
        list.add(new Person("i", 18, "杭州", 999.9));
        list.add(new Person("am", 19, "温州", 777.7));
        list.add(new Person("iron", 21, "杭州", 888.8));
        list.add(new Person("man", 17, "宁波", 888.8));
    }

    public static void main(String[] args) {
        // JDK8之前：Collections工具类+匿名内部类。Collections类似于Arrays工具类，我经常用Arrays.asList(）
        Collections.sort(list, new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return p1.getName().length()-p2.getName().length();
            }
        });
        
        // JDK8之前：List本身也实现了sort()
        list.sort(new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return p1.getName().length()-p2.getName().length();
            }
        });
        
        // JDK8之后：Lambda传参给Comparator接口，其实就是实现Comparator#compare()。注意，equals()是Object的，不妨碍
        list.sort((p1,p2)->p1.getName().length()-p2.getName().length());
        
        // JDK8之后：使用JDK1.8为Comparator接口新增的comparing()方法

        list.sort(Comparator.comparingInt(p -> p.getName().length()));
        System.out.println(list);
        list.sort(Comparator.comparing(person -> person.getName().length(),Comparator.reverseOrder()));
        System.out.println(list);
        List<String> result1 = list.stream()
                .map(Person::getName)
                .sorted(Comparator.comparing(t -> t, (str1, str2) -> str2.length() - str1.length()))
                .collect(Collectors.toList());
        System.out.println(result1);



    }

    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Person {
        private String name;
        private Integer age;
        private String address;
        private Double salary;
    }
}