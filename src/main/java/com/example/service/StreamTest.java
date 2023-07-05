package com.example.service;



import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.Function;
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

    public static void main(String[] args) throws JsonProcessingException {
        // 最常用的4个方法

        // 把结果收集为List
        List<String> toList = list.stream().map(Person::getAddress).collect(Collectors.toList());
        System.out.println(toList);
        
        // 把结果收集为Set
        Set<String> toSet = list.stream().map(Person::getAddress).collect(Collectors.toSet());
        System.out.println(toSet);
        
        // 把结果收集为Map，前面的是key，后面的是value，如果你希望value是具体的某个字段，可以改为toMap(Person::getName, person -> person.getAge())
        Map<String, Person> nameToPersonMap = list.stream()
                .collect(Collectors.toMap(Person::getName, person -> person,(preKey, nextKey) -> nextKey));
        System.out.println(nameToPersonMap);

        // 把结果收集起来，并用指定分隔符拼接
        String result = list.stream().map(Person::getAddress).collect(Collectors.joining("~"));
        System.out.println(result);

        //聚合：max/min/count
        Optional<Integer> max = list.stream().map(Person::getAge).max(Integer::compareTo);
        System.out.println(max.orElse(0));

        //字段分组
        Map<String, List<Person>> collect = list.stream().collect(Collectors.groupingBy(Person::getAddress));
        System.out.println(collect);
        // GROUP BY address, age
        Map<String, Map<Integer, List<Person>>> nestedCollect = list.stream().collect(Collectors.groupingBy(Person::getAddress, Collectors.groupingBy(Person::getAge)));
//        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(nestedCollect));
        System.out.println(JSON.toJSONString(nestedCollect));
        // 解决了按字段分组、按多个字段分组，我们再考虑一个问题：有时我们分组的条件不是某个字段，而是某个字段是否满足xx条件
        // 比如 年龄大于等于18的是成年人，小于18的是未成年人
        Map<Boolean, List<Person>> partitionCollect = list.stream().collect(Collectors.partitioningBy(person -> person.getAge() >= 18));
        System.out.println(partitionCollect);

        //统计 年龄 工资
        System.out.println(list.stream().collect(Collectors.averagingInt(Person::getAge)));
        System.out.println(list.stream().collect(Collectors.averagingDouble(Person::getSalary)));

        //1.要求分组统计出各个城市的年龄总和，返回格式为 Map<String, Integer>。
        //2.要求得到Map<城市, List<用户工资>>
        Map<String, Integer> collect1 = list.stream().collect(Collectors.groupingBy(Person::getAddress, Collectors.summingInt(Person::getAge)));
        Map<String, List<Double>> collect2 = list.stream().collect(Collectors.groupingBy(Person::getAddress, Collectors.mapping(Person::getSalary, Collectors.toList())));
        System.out.println(JSON.toJSONString(collect1));
        System.out.println(JSON.toJSONString(collect2));


        //根据address去重
        TreeSet<Person> collect3 = list.stream().collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Person::getAddress))));
        System.out.println(collect3);
        //冲突保留后者。
        Map<String, Person> collect4 = list.stream().collect(Collectors.toMap(Person::getAddress, Function.identity(), (pre, next) -> next));
        ArrayList<Person> list = new ArrayList<>(collect4.values());
        System.out.println(list);

        Collection<Person> collect5 = list.stream().collect(Collectors.collectingAndThen(Collectors.toMap(Person::getAddress, Function.identity(), (pre, next) -> next), Map::values));
        System.out.println(collect5);

        ArrayList<Person> collect6 = (ArrayList<Person>) list.stream().collect(Collectors.collectingAndThen(Collectors.toMap(Person::getAddress, Function.identity(), (pre, next) -> next), Map::values));
        System.out.println(collect6);



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