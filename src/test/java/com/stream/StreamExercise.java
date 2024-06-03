package com.stream;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Author Liwei
 * @Date 2021/7/11 20:52
 */
public class StreamExercise {
    @Data
    @AllArgsConstructor
    static class Person {
        private String name;
        private Integer age;
        private String address;
        private Double salary;
    }

    private static List<Person> list;

    static {
        list = new ArrayList<>();
        list.add(new Person("i", 18, "杭州", 999.9));
        list.add(new Person("am", 19, "温州", 777.7));
        list.add(new Person("iron", 21, "杭州", 888.8));
        //list.add(new Person("iron", 17, "宁波", 888.8));
        list.add(new Person("iron", 17, "宁波", 888.8));
    }

    @Test
    public void test1() {
        List<String> collect1 = list.stream().map(person -> person.getName() + "来自" + person.getAddress()).collect(Collectors.toList());
        System.out.println(collect1);
        System.out.println("===========================");
        List<Person> collect2 = list.stream().filter(person -> person.getAge() >= 18).filter(person -> person.getSalary() >= 888.8).filter(person -> person.getAddress().equals("杭州")).collect(Collectors.toList());
        System.out.println(collect2);
        System.out.println("===========================");
        List<Person> collect3 = list.stream().filter(person -> person.getAge() >= 18 && person.getSalary() >= 888.8 && "杭州".equals(person.getAddress())).collect(Collectors.toList());
        System.out.println(collect3);
        System.out.println("===========================");
        OptionalDouble average = list.stream().mapToDouble(Person::getSalary).average();
        System.out.println(average.getAsDouble());
        System.out.println("===========================");
        Optional<Person> first = list.stream().filter(person -> "宁波".equals(person.getAddress())).findFirst();
        first.ifPresent(System.out::println);
        System.out.println("===========================");
        boolean flag = list.stream().anyMatch(person -> "宁波".equals(person.getAddress()));
        System.out.println(flag);

    }

    @Test
    public void test2() throws JsonProcessingException {
        List<String> collect = list.stream().filter(person -> person.getAge() >= 18).sorted(Comparator.comparingInt(Person::getAge)).map(Person::getName).limit(2).collect(Collectors.toList());
        System.out.println(collect);
        System.out.println("===========================");
        Map<String, Person> personMap = list.stream().collect(Collectors.toMap(Person::getName, person -> person, (pre, next) -> pre));
        System.out.println(personMap);
        System.out.println("===========================");
        Map<String, String> personMap2 = list.stream().collect(Collectors.toMap(Person::getName, Person::getAddress, (pre, next) -> pre));
        System.out.println(personMap2);
        System.out.println("===========================");
        HashSet<Person> personHashSet = new HashSet<>(list);
        System.out.println(personHashSet);
        System.out.println(personHashSet.size());
        System.out.println("===========================");
        Set<Person> streamSet = list.stream().distinct().collect(Collectors.toSet());
        System.out.println(streamSet);
        System.out.println("===========================");
        Map<String, Person> nameMap = list.stream().collect(Collectors.toMap(Person::getName, p -> p, (pre, next) -> pre));
        ArrayList<Person> personArrayList = new ArrayList<>(nameMap.values());
        System.out.println("===========================");
        Map<String, List<Person>> result = list.stream().collect(Collectors.groupingBy(Person::getAddress));
        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(result));
        System.out.println("===========================");
        Map<String, Set<Person>> setMap = list.stream().collect(Collectors.groupingBy(Person::getAddress, Collectors.toSet()));
        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(setMap));

        System.out.println("===========================");
        // 按照address分组，提取age
        Map<String, List<Integer>> result2 = list.stream().collect(Collectors.groupingBy(
                Person::getAddress,
                Collectors.mapping(Person::getAge, Collectors.toList())));
        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(result2));

        //按照age分组并排序(逆序)，提取name
        TreeMap<Integer, List<String>> result3 = list.stream().collect(
                Collectors.groupingBy(Person::getAge,
                ()-> new TreeMap<>(Comparator.reverseOrder()),
                Collectors.mapping(Person::getName, Collectors.toList()))
        );
        System.out.println(JSON.toJSONString(result3));


        System.out.println("===========================");
        Map<Boolean, List<Person>> partitionCollect = list.stream()
                .collect(Collectors.partitioningBy(person -> person.getAge() > 18 && "杭州".equals(person.getAddress())));
        System.out.println(partitionCollect);
        System.out.println("===========================");
        List<String> top2Adult = list.stream()
                .filter(person -> person.getAge() >= 18)            // 过滤得到年龄大于等于18岁的人
                .sorted(Comparator.comparingInt(Person::getAge))    // 按年龄排序
                .map(Person::getName)                               // 得到姓名
                .limit(2)                                           // 取前两个数据
                .collect(Collectors.toCollection(LinkedList::new)); // 返回LinkedList，其他同理
        System.out.println(top2Adult);
    }


    @Test
    public void distinctTest(){
        // Assuming your list is already initialized and populated
        List<Person> distinctByName = list.stream()
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Person::getName))))
                .stream()
                .collect(Collectors.toList());
        System.out.println(distinctByName);

        List<Person> collect = list.stream()
                .collect(Collectors.toMap(Person::getName, person -> person, (existing, replacement) -> existing))
                .values().stream()
                .collect(Collectors.toList());
    }

    @Test
    public void distinctTest2(){
        List<Person> distinctByName = list.stream()
                .filter(distinctByKey(Person::getName))
                .collect(Collectors.toList());
        System.out.println(distinctByName);
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @Test
    public void distinctTest3(){
        //Collectors.collectingAndThen()
        Collection<Person> collect = list.stream().collect(Collectors.collectingAndThen(Collectors.toMap(Person::getAddress, Function.identity(), (pre, next) -> next), Map::values));
        ArrayList<Person> res = Lists.newArrayList(collect);
        System.out.println(res);
    }
}
