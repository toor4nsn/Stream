package com.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Liwei
 * @Date 2021/7/12 13:03
 */
public class demo4 {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Dish {
        private String id;
        private String name;
    }
    private static List<Dish> dishList = new ArrayList<Dish>();
    static {
        Dish dish1 = new Dish("001", "张三");
        dishList.add(dish1);
        Dish dish2 = new Dish("001", "李四");
        dishList.add(dish2);
        Dish dish3 = new Dish("002", "王五");
        dishList.add(dish3);
    }
    @Test
    public void test(){
        TreeSet<Dish> treeSet = new TreeSet<>(Comparator.comparing(Dish::getId));
        treeSet.addAll(dishList);
        System.out.println(treeSet);
        System.out.println("===========");
        ArrayList<Dish> collect = dishList.stream()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Dish::getId))), ArrayList::new));
        System.out.println(collect);
        System.out.println("===========");
        List<Dish> toor4nsn = dishList.stream().peek(x -> x.setName("toor4nsn")).collect(Collectors.toList());
        System.out.println(toor4nsn);
    }
}
