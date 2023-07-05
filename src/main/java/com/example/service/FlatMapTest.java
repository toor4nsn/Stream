package com.example.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlatMapTest {

    private static List<Person> list;

    static {
        list = new ArrayList<>();
        list.add(new Person("i", 18, "杭州", 999.9, new ArrayList<>(Arrays.asList("成年人", "学生", "男性"))));
        list.add(new Person("am", 19, "温州", 777.7, new ArrayList<>(Arrays.asList("成年人", "打工人", "宇宙最帅"))));
        list.add(new Person("iron", 21, "杭州", 888.8, new ArrayList<>(Arrays.asList("喜欢打篮球", "学生"))));
        list.add(new Person("man", 17, "宁波", 888.8, new ArrayList<>(Arrays.asList("未成年人", "家里有矿"))));
    }

    public static void main(String[] args) {
        Set<String> allTags = list.stream()
                .flatMap(person -> person.getTags().stream())
                .collect(Collectors.toSet());
        System.out.println(allTags);
        System.out.println("----------");
        List<List<String>> tag1 = list.stream().map(person -> person.getTags()).collect(Collectors.toList());
        Set<String> set = new HashSet<>();
        for (List<String> strList : tag1) {
            set.addAll(strList);
        }
        System.out.println(tag1);
        System.out.println("----------");
        System.out.println(set);
        System.out.println("----------");
        Set<String> collect = list.stream().flatMap(person -> person.getTags().stream()).collect(Collectors.toSet());
        System.out.println(collect);
        System.out.println("----------");
        Set<String> collect2 = list.stream().map(Person::getTags).flatMap(Collection::stream).collect(Collectors.toSet());
        System.out.println(collect2);
        System.out.println(list.stream().map(Person::getTags).flatMap(Collection::stream).collect(Collectors.toList()));

        Map<Long, List<Long>> map = new HashMap<>();
        map.put(1L, new ArrayList<>(Arrays.asList(1L, 2L, 3L)));
        map.put(2L, new ArrayList<>(Arrays.asList(4L, 5L, 6L)));
        ArrayList<Long> keyList = new ArrayList<>(map.keySet());
        Stream<List<Long>> stream = map.values().stream();
        List<Long> longList = map.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        System.out.println(longList);
    }
    

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Person {
        private String name;
        private Integer age;
        private String address;
        private Double salary;
        // 个人标签
        private List<String> tags;
    }
}