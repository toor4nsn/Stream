package com.example.util;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ConvertUtil {

    private ConvertUtil() {
    }

    /**
     * 将List转为Map
     *
     * @param list         原数据
     * @param keyExtractor Key的抽取规则
     */
    public static <K, V> Map<K, V> listToMap(List<V> list, Function<V, K> keyExtractor) {
        return ConvertUtil.listToMap(list, keyExtractor, v -> v);
    }

    /**
     * 将List转为Map，同时支持自定义key和value
     *
     * @param list           原数据
     * @param keyExtractor   Key的抽取规则
     * @param valueExtractor Value的抽取规则
     */
    public static <K, V, R> Map<K, R> listToMap(List<V> list, Function<V, K> keyExtractor, Function<V, R> valueExtractor) {
        if (list == null || list.isEmpty()) {
            return new HashMap<>(0);
        }
        Map<K, R> map = new HashMap<>(list.size());
        for (V element : list) {
            K key = keyExtractor.apply(element);
            if (key == null) {
                continue;
            }
            map.put(key, valueExtractor.apply(element));
        }
        return map;
    }

    /**
     * 将List映射为List，比如List<Person> personList转为List<String> nameList
     *
     * @param originList 原数据
     * @param mapper     映射规则
     */
    public static <T, R> List<R> resultToList(List<T> originList, Function<T, R> mapper) {
        if (originList == null || originList.isEmpty()) {
            return new ArrayList<>(0);
        }
        List<R> newList = new ArrayList<>(originList.size());
        for (T originElement : originList) {
            R newElement = mapper.apply(originElement);
            if (newElement == null) {
                continue;
            }
            newList.add(newElement);
        }
        return newList;
    }

    /**
     * 将List映射为List，比如List<Person> personList转为List<String> nameList
     * 可以指定过滤规则
     *
     * @param originList 原数据
     * @param predicate  过滤规则
     * @param mapper     映射规则
     */
    public static <T, R> List<R> filterToList(List<T> originList, Predicate<T> predicate, Function<T, R> mapper) {
        if (originList == null || originList.isEmpty()) {
            return new ArrayList<>(0);
        }
        List<R> newList = new ArrayList<>(originList.size());
        for (T originElement : originList) {
            R newElement = mapper.apply(originElement);
            if (newElement == null || !predicate.test(originElement)) {
                continue;
            }
            newList.add(newElement);
        }
        return newList;
    }

    /**
     * 安全的foreach
     *
     * @param originList 需要遍历的List
     * @param processor  需要执行的操作
     */
    public static <T> void foreachIfNonNull(List<T> originList, Consumer<T> processor) {
        if (originList == null || originList.isEmpty()) {
            return;
        }
        for (T originElement : originList) {
            if (originElement == null) {
                continue;
            }
            processor.accept(originElement);
        }
    }

    /**
     * groupBy分组，比如 List(User)，分组后变成Map(age, List(user))
     *
     * @param originList   需要分组的List
     * @param keyExtractor 分组规则（key）
     * @return
     */
    public static <T, K> Map<K, List<T>> groupingBy(List<T> originList, Function<T, K> keyExtractor) {
        return ConvertUtil.groupingBy(originList, keyExtractor, v -> v);
    }

    /**
     * groupBy分组，比如 List(User)，分组后变成Map(age, List(username))
     *
     * @param originList     需要分组的List
     * @param keyExtractor   分组规则（key）
     * @param valueExtractor 分组List值的抽取规则
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K, V> Map<K, List<V>> groupingBy(List<T> originList, Function<T, K> keyExtractor, Function<T, V> valueExtractor) {
        if (originList == null || originList.isEmpty()) {
            return new HashMap<>(0);
        }

        Map<K, List<V>> map = new HashMap<>(originList.size());
        for (T element : originList) {
            K key = keyExtractor.apply(element);
            if (key == null) {
                continue;
            }

            List<V> list = map.computeIfAbsent(key, k -> new ArrayList<>());
            list.add(valueExtractor.apply(element));
        }

        return map;
    }

    /**
     * 去重（保持顺序）
     *
     * @param originList           原数据
     * @param distinctKeyExtractor 去重规则
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K> List<T> distinct(List<T> originList, Function<T, K> distinctKeyExtractor) {
        LinkedHashMap<K, T> resultMap = new LinkedHashMap<>(originList.size());
        for (T item : originList) {
            K distinctKey = distinctKeyExtractor.apply(item);
            if (resultMap.containsKey(distinctKey)) {
                continue;
            }
            resultMap.put(distinctKey, item);
        }
        return new ArrayList<>(resultMap.values());
    }

}