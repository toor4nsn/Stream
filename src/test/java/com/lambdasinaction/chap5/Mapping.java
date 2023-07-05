package com.lambdasinaction.chap5;

import com.lambdasinaction.chap4.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static com.lambdasinaction.chap4.Dish.menu;

public class Mapping{

    public static void main(String...args){

        // map
        List<String> dishNames = menu.stream()
                                     .map(Dish::getName)
                                     .collect(toList());
        System.out.println(dishNames);

        // map
        List<String> words = Arrays.asList("Hello", "World");
        List<Integer> wordLengths = words.stream()
                                         .map(String::length)
                                         .collect(toList());
        System.out.println(wordLengths);

        List<String> splitWord = words.stream()
                .map(item -> item.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(toList());
        List<String> splitWord2 = words.stream().flatMap(item -> Arrays.stream(item.split(""))).distinct().collect(toList());


        // flatMap
        words.stream()
                 .flatMap((String line) -> Arrays.stream(line.split("")))
                 .distinct()
                 .forEach(System.out::println);

        List<String> collect = words.stream().map(item -> item.split("")).flatMap(Arrays::stream).distinct().collect(toList());
        System.out.println("collect:"+collect);
        List<Integer> list1 = Stream.of(1, 2, 3, 4, 5).map(it -> it * it).collect(toList());
        System.out.println("list1:"+list1);

        // flatMap
        List<Integer> numbers1 = Arrays.asList(1,2,3,4,5);
        List<Integer> numbers2 = Arrays.asList(6,7,8);

        List<int[]> list3 = numbers1.stream()
                .flatMap(i -> numbers2.stream().map(j -> new int[]{i, j}))
                .collect(toList());


        List<int[]> pairs =
                        numbers1.stream()
                                .flatMap((Integer i) -> numbers2.stream().map((Integer j) -> new int[]{i, j}))
                                .filter(pair -> (pair[0] + pair[1]) % 3 == 0)
                                .collect(toList());
        pairs.forEach(pair -> System.out.println("(" + pair[0] + ", " + pair[1] + ")"));

        System.out.println("-------");
        List<int[]> list = numbers1.stream().flatMap(i -> numbers2.stream().map(j -> new int[]{i, j})).collect(toList());
        list.forEach(pair -> System.out.println("(" + pair[0] + ", " + pair[1] + ")"));

        List<int[]> list2 = numbers1
                .stream()
                .flatMap(i -> numbers2.stream().map(j -> new int[]{i, j}))
                .filter(item -> item[0] + item[1] % 3 == 0)
                .collect(toList());

    }
}
