package com.lambdasinaction.chap6;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;
import static com.lambdasinaction.chap6.Dish.menu;

public class Partitioning {

    public static void main(String ... args) {
        System.out.println("Dishes partitioned by vegetarian: " + partitionByVegeterian());
        System.out.println("Vegetarian Dishes by type: " + vegetarianDishesByType());
        System.out.println("Most caloric dishes by vegetarian: " + mostCaloricPartitionedByVegetarian());
    }

    private static Map<Boolean, List<Dish>> partitionByVegeterian() {
        return menu.stream().collect(partitioningBy(Dish::isVegetarian));
    }

    private static Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType() {
        return menu.stream().collect(partitioningBy(Dish::isVegetarian, groupingBy(Dish::getType)));
    }

    private static Object mostCaloricPartitionedByVegetarian() {
        return menu.stream().collect(
                partitioningBy(Dish::isVegetarian,
                        collectingAndThen(
                                maxBy(comparingInt(Dish::getCalories)),
                                Optional::get)));
    }

    @Test
    public void test(){


        //分区
        //素食和非素食分开
        Map<Boolean, List<Dish>> isVegetarian = menu.stream().collect(partitioningBy(Dish::isVegetarian));
        System.out.println("isVegetarian：" + isVegetarian);
        //找出所有的素食菜肴
        List<Dish> vegetarian = isVegetarian.get(true);
        System.out.println("vegetarian：" + vegetarian);

        //第一层：是否是素食，第二层：菜肴类型
        Map<Boolean, Map<Dish.Type, List<Dish>>> multiPartitioningBy = menu.stream().collect(partitioningBy(Dish::isVegetarian, groupingBy(Dish::getType)));
        System.out.println("multiPartitioningBy:"+multiPartitioningBy);

        //找到素食和非素食中热量最高的菜，第一层：是否是素食，第二层：热量最高的菜
        Map<Boolean, Dish> isVegetarianMaxCalories = menu.stream()
                .collect(partitioningBy(Dish::isVegetarian, collectingAndThen(maxBy(Comparator.comparingInt(Dish::getCalories)), Optional::get)));
        System.out.println("isVegetarianMaxCalories："+isVegetarianMaxCalories);

        //第一层：是否是素食，第二层：按照是否是素食分类的数量
        Map<Boolean, Long> isVegetarianCount = menu.stream().collect(partitioningBy(Dish::isVegetarian, counting()));
        System.out.println("isVegetarianCount："+ isVegetarianCount);

        //素数分区
        Map<Boolean, List<Integer>> map = partitionPrime(50);
        System.out.println(map);
    }

    /**
     * 是否是质数
     * 仅测试小于等于待测数平方根的因子
     * @param candidate
     * @return
     */
    public boolean isPrime(int candidate) {
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return IntStream.rangeClosed(2, candidateRoot)
                .noneMatch(i -> candidate % i == 0);
    }

    public Map<Boolean,List<Integer>> partitionPrime(int n){
        Map<Boolean, List<Integer>> collect = IntStream.rangeClosed(2, n).boxed().collect(partitioningBy(this::isPrime));
        return collect;
    }
}

