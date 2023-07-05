package com.stream;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author Liwei
 * @Date 2021/7/10 0:37
 */

public class demo3 {
    @Test
    public void test1(){
        ArrayList<Person> uncheckPersonList = new ArrayList<>();
        uncheckPersonList.add(new Person("张三", 18));
        uncheckPersonList.add(new Person("李四", 20));

        //demo3.getHealthPerson(uncheckPersonList,person -> 核酸检测(person));
        //demo3.getHealthPerson(uncheckPersonList,person -> CT(person));
    }
    public static List<Person> getHealthPerson(List<Person> uncheckedPersonList,Predicate<Person> predicate){
        ArrayList<Person> healthyPersonList = new ArrayList<>();
        for (Person person : uncheckedPersonList) {
            if (predicate.test(person)){
                healthyPersonList.add(person);
            }
        }
        return healthyPersonList;
    }
    @Test
    public void test2(){
        List<String> collect = Stream.of("one", "two", "three", "four")
                .filter(e -> e.length() > 3)
                .peek(e -> System.out.println("Filtered value: " + e))
                .map(String::toUpperCase)
                .peek(e -> System.out.println("Mapped value: " + e))
                .collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    public void test3(){
        ArrayList<Integer> list = new ArrayList<>();
        ArrayList<Integer> list2 = new ArrayList<>();
        Collections.addAll(list2,1,9,9,5);
        Collections.addAll(list,2,0,0,8);
        //list.add(list2);
        //System.out.println(list);
        System.out.println("--------------------");
        list.addAll(list2);
        System.out.println(list);
        List<String> strList = list2.stream().map(String::valueOf).collect(Collectors.toList());
        Optional<Integer> any = list.parallelStream().findAny();
        System.out.println(any.get());
    }
    @Test
    public void whenFlatMapEmployeeNames_thenGetNameStream() {
        List<List<String>> namesNested = Arrays.asList(
                Arrays.asList("Jeff", "Bezos"),
                Arrays.asList("Bill", "Gates"),
                Arrays.asList("Mark", "Zuckerberg"));

        List<String> namesFlatStream = namesNested.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        System.out.println(namesFlatStream);
        System.out.println("=============");
    }
    @Test
    public void test4(){
        List<String> strings = Arrays.asList("Hello", "World");
        List<String> list = strings
                .stream() // 将集合转成流
                .map(s -> s.split("")) // 转换成['H','e','l','l','o'],['W','o','r','l','d'] 两个数组
                .flatMap(Arrays::stream) // 将两个数组扁平化成为['H','e','l','l','o','W','o','r','l','d']，实际上还是把两个数组再次转成流
                .distinct() // 去除重复元素
                .collect(Collectors.toList()); // 终端操作，转化成集合
        System.out.println(list);
// 输出: [H, e, l, o, W, r, d]

    }

    @Test
    public void test5(){
        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(4, 5, 6);
        List<Integer> collect = Stream.of(numbers1, numbers2) // 将两个集合转成流
                .flatMap(numbers -> numbers.stream()) // 两个集合流扁平化为[1,2,3,4,5,6]
                .collect(Collectors.toList());
        System.out.println(collect);
        // 输出: 1，2，3，4，5，6
    }
    @Test
    public void test6(){
        Optional<Integer> first = Stream.of(1, 2, 3, 4)
                .peek(v -> System.out.print(v + ","))
                .findFirst();
        System.out.println(first.get());
        Optional<Integer> any = Stream.of(1, 2, 3, 4)
                .peek(v -> System.out.print(v + ","))
                .findAny();
        System.out.println(any.get());

        boolean flag1 = Stream.of(1, 2, 3, 4)
                .peek(v -> System.out.print(v + ","))
                .allMatch(v -> v > 2);
        System.out.println(flag1);
        boolean flag2 = Stream.of(1, 2, 3, 4)
                .peek(v -> System.out.print(v + ","))
                .noneMatch(v -> v >= 2);
        System.out.println(flag2);
        boolean flag3 = Stream.of(1, 2, 3, 4)
                .peek(v -> System.out.print(v + ","))
                .anyMatch(v -> v > 2);
        System.out.println(flag3);
    }
}
