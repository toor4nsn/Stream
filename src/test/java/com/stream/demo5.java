package com.stream;



import com.alibaba.fastjson.JSON;
import com.example.po.Item;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Liwei
 * @Date 2021/7/12 19:17
 */
public class demo5 {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Student implements Serializable {
        private String name;
        private int age;
        private String department;
    }




    @Test
    public void test1(){
        List<Student> list = Lists.newArrayList(
                new Student("John", 19, "CS"),
                new Student("Adam", 21, "Math"),
                new Student("Steve", 20, "CS"),
                new Student("Perry", 22, "Science"),
                new Student("Kyle", 20, "Math")
        );
        System.out.println(list);
        Map<String, List<String>> collect1 = list.stream().collect(Collectors.groupingBy(Student::getDepartment, Collectors.mapping(Student::getName, Collectors.toList())));
        String s1 = JSON.toJSONString(collect1);
        System.out.println(s1);
        System.out.println("----------------------");

        Map<String, List<String>> collect2 = list.stream().collect(Collectors.groupingBy(Student::getDepartment, Collectors.collectingAndThen(Collectors.mapping(Student::getName, Collectors.toList()), Collections::unmodifiableList)));
        System.out.println(JSON.toJSONString(collect2));
        Set<Map.Entry<String, List<String>>> entries = collect1.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            String key = entry.getKey();
            entry.getValue().remove(0);
        }

        System.out.println(JSON.toJSONString(collect1));
    }


    @Test
    public void groupingByTest(){
        List<Item> list = Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orange", 10, new BigDecimal("9.99")),
                new Item("orange", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99")),
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 10, new BigDecimal("19.99")),
                new Item("apple", 20, new BigDecimal("9.99"))
        );
        //name进行分组，然后统计每个分组得总数量
        Map<String, Integer> var1 = list.stream().collect(Collectors.groupingBy(Item::getName, Collectors.summingInt(Item::getQty)));
        System.out.println(var1);
        //price进行分组，然后提取对应的name
        Map<BigDecimal, Set<String>> var2 = list.stream().collect(Collectors.groupingBy(Item::getPrice, Collectors.mapping(Item::getName, Collectors.toSet())));
        System.out.println(var2);

        //price进行分组，对分组后的商品名称按价格进行排序
        Map<BigDecimal, Set<String>> var3 = list.stream().collect(Collectors.groupingBy(Item::getPrice, TreeMap::new, Collectors.mapping(Item::getName, Collectors.toSet())));
        System.out.println(var3);

        //name进行分组，查找每个子组中 price 最高的
        Map<String, Item> var4 = list.stream().collect(Collectors.groupingBy(Item::getName,
                Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(Item::getPrice)), Optional::get)));
        System.out.println(var4);

        //name进行分组，分组中按照 qty 排序
        Map<String, List<Item>> var5 = list.stream().collect(Collectors.groupingBy(Item::getName,
                Collectors.collectingAndThen(Collectors.toList(), it -> it.stream().sorted(Comparator.comparingInt(Item::getQty)).collect(Collectors.toList()))));
        System.out.println(JSON.toJSONString(var5));

    }




    
}
