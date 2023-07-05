package com.lambdasinaction.chap6;


import com.alibaba.fastjson2.JSON;
import org.apache.logging.log4j.message.Message;
import org.junit.jupiter.api.Test;

import javax.sound.midi.Soundbank;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;
import static com.lambdasinaction.chap6.Dish.menu;

public class Grouping {

    enum CaloricLevel {DIET, NORMAL, FAT}

    ;

    public static void main(String... args) {
        System.out.println("Dishes grouped by type: " + groupDishesByType());
        System.out.println("Dishes grouped by caloric level: " + groupDishesByCaloricLevel());
        System.out.println("Dishes grouped by type and caloric level: " + groupDishedByTypeAndCaloricLevel());
        System.out.println("Count dishes in groups: " + countDishesInGroups());
        System.out.println("Most caloric dishes by type: " + mostCaloricDishesByType());
        System.out.println("Most caloric dishes by type: " + mostCaloricDishesByTypeWithoutOprionals());
        System.out.println("Sum calories by type: " + sumCaloriesByType());
        System.out.println("Caloric levels by type: " + caloricLevelsByType());
    }

    private static Map<Dish.Type, List<Dish>> groupDishesByType() {
        return menu.stream().collect(groupingBy(Dish::getType));
    }

    private static Map<CaloricLevel, List<Dish>> groupDishesByCaloricLevel() {
        return menu.stream().collect(
                groupingBy(dish -> {
                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                    else return CaloricLevel.FAT;
                }));
    }

    private static Map<Dish.Type, Map<CaloricLevel, List<Dish>>> groupDishedByTypeAndCaloricLevel() {
        return menu.stream().collect(
                groupingBy(Dish::getType,
                        groupingBy((Dish dish) -> {
                            if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                            else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                            else return CaloricLevel.FAT;
                        })
                )
        );
    }

    private static Map<Dish.Type, Long> countDishesInGroups() {
        return menu.stream().collect(groupingBy(Dish::getType, counting()));
    }

    private static Map<Dish.Type, Optional<Dish>> mostCaloricDishesByType() {
        return menu.stream().collect(
                groupingBy(Dish::getType,
                        reducing((Dish d1, Dish d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2)));
    }

    private static Map<Dish.Type, Dish> mostCaloricDishesByTypeWithoutOprionals() {
        return menu.stream().collect(
                groupingBy(Dish::getType,
                        collectingAndThen(
                                reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2),
                                Optional::get)));
    }

    private static Map<Dish.Type, Integer> sumCaloriesByType() {
        return menu.stream().collect(groupingBy(Dish::getType,
                summingInt(Dish::getCalories)));
    }

    private static Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType() {
        return menu.stream().collect(
                groupingBy(Dish::getType, mapping(
                        dish -> {
                            if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                            else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                            else return CaloricLevel.FAT;
                        },
                        toSet())));
    }

    @Test
    public void test() {
        Map<CaloricLevel, List<Dish>> collect = menu.stream().collect(groupingBy((dish) -> {
            if (dish.getCalories() <= 400) {
                return CaloricLevel.DIET;
            } else if (dish.getCalories() <= 700) {
                return CaloricLevel.NORMAL;
            } else {
                return CaloricLevel.FAT;
            }
        }));
        System.out.println(collect);

        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> collect1 = menu.stream().collect(groupingBy(Dish::getType, groupingBy((dish) -> {
            if (dish.getCalories() <= 400) {
                return CaloricLevel.DIET;
            } else if (dish.getCalories() <= 700) {
                return CaloricLevel.NORMAL;
            } else {
                return CaloricLevel.FAT;
            }
        })));
        System.out.println(JSON.toJSONString(collect1));

        //要数一数菜单中每类菜有多少个
        Map<Dish.Type, Long> collect2 = menu.stream().collect(groupingBy(Dish::getType, counting()));
        System.out.println(collect2);

        Map<Dish.Type, Optional<Dish>> collect3 = menu.stream().collect(groupingBy(Dish::getType, maxBy(Comparator.comparingInt(Dish::getCalories))));
        System.out.println(collect3);

        Map<Dish.Type, Optional<Dish>> collect4 = menu.stream().collect(groupingBy(Dish::getType, reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2)));
        System.out.println(collect4);

        Map<Dish.Type, Dish> collect5 =
                menu.stream().collect(Collectors.toMap(Dish::getType, Function.identity(), BinaryOperator.maxBy(Comparator.comparingInt(Dish::getCalories)),TreeMap::new));
        System.out.println(collect5);


        Map<Dish.Type, Integer> collect6 = menu.stream().collect(groupingBy(Dish::getType, summingInt(Dish::getCalories)));
        System.out.println(collect6);


        Map<Dish.Type, Set<CaloricLevel>> collect7 = menu.stream().collect(groupingBy(Dish::getType, mapping((dish) -> {
            if (dish.getCalories() <= 400) {
                return CaloricLevel.DIET;
            } else if (dish.getCalories() <= 700) {
                return CaloricLevel.NORMAL;
            } else {
                return CaloricLevel.FAT;
            }
        }, toSet())));
        System.out.println(collect7);

    }

    @Test
    public void mytest(){
        Map<Dish.Type, List<Dish>> collect = menu.stream().collect(groupingBy(Dish::getType));
        System.out.println(collect);

        //菜肴的热量水平进行分组
        Map<CaloricLevel, List<Dish>> caloricLevelMap = menu.stream().collect(groupingBy(Grouping::getCaloricLevel));


        //找出那些热量大于500卡路里的菜肴
        //Java 9中Collectors.filtering()方法
//        Map<Dish.Type, List<Dish>> caloricDishesByType =
//                menu.stream().collect(groupingBy(Dish::getType, filtering(dish -> dish.getCalories() > 500, toList())));


//        System.out.println("Caloric levels by type: " + caloricLevelsByType());

        //按照菜肴的类型进行分类，并且提取出菜肴的名字
        Map<Dish.Type, List<String>> typeNameMap = menu.stream().collect(groupingBy(Dish::getType, mapping(Dish::getName, toList())));
        System.out.println(typeNameMap);

        //多级分组
        //1.一层：菜肴类型，第二层：菜肴的热量水平
        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> multiGroupBy = menu.stream()
                .collect(groupingBy(Dish::getType, groupingBy(Grouping::getCaloricLevel)));
        System.out.println(multiGroupBy);

        //1.一层：菜肴类型，第二层：每种菜肴类型的数量
        Map<Dish.Type, Long> typeNumber = menu.stream().collect(groupingBy(Dish::getType, counting()));
        System.out.println(typeNumber);

        //1.一层：菜肴类型，第二层：热量最高的菜肴
        Map<Dish.Type, Optional<Dish>> typeMaxCaloriesOptional = menu.stream().collect(groupingBy(Dish::getType, maxBy(Comparator.comparingInt(Dish::getCalories))));
        //去掉values里面的optional
        Map<Dish.Type, Dish> typeMaxCalories = typeMaxCaloriesOptional.values().stream().map(item -> item.get()).collect(toMap(Dish::getType, t -> t));
        System.out.println("typeMaxCaloriesOptional:"+typeMaxCaloriesOptional);
        System.out.println("typeMaxCalories:"+typeMaxCalories);


        //Collectors.collectingAndThen:把收集器返回的结果转换为另一种类型
        Map<Dish.Type, Dish> typeMaxCalories2 = menu.stream()
                .collect(groupingBy(Dish::getType,
                                    collectingAndThen(maxBy(Comparator.comparingInt(Dish::getCalories)), Optional::get)));
        System.out.println("typeMaxCalories2:"+typeMaxCalories2);


        //一层：菜肴类型， 第二层：菜肴热量总和
        Map<Dish.Type, Integer> typeCaloriesSum = menu.stream().collect(groupingBy(Dish::getType, summingInt(Dish::getCalories)));
        System.out.println("typeCaloriesSum:"+typeCaloriesSum);

        //一层：菜肴类型， 第二层：CaloricLevel
        Map<Dish.Type, Set<CaloricLevel>> typeCaloricLevel = menu.stream()
                .collect(groupingBy(Dish::getType, mapping(Grouping::getCaloricLevel, toSet())));
        System.out.println("typeCaloricLevel"+typeCaloricLevel);




    }



    /**
     * 根据菜单的卡路里量进行菜单分类
     * @param item
     * @return
     */
    private static CaloricLevel getCaloricLevel(Dish item) {
        if (item.getCalories() <= 400) {
            return CaloricLevel.DIET;
        } else if (item.getCalories() <= 700) {
            return CaloricLevel.NORMAL;
        } else {
            return CaloricLevel.FAT;
        }
    }




}
