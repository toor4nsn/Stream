package com.generic;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Liwei
 * @Date 2021/8/15 23:37
 */
public class DemoForGenericMethod {
    public static void main(String[] args) {
        ArrayList<Integer> a = Lists.newArrayList(1, 2, 3, 4);
        List<Integer> b = reverseList(a);
        System.out.println(b);
        System.out.println("------------");
        ArrayList<String> c = Lists.newArrayList("a", "b", "c");
        List<String> d = reverseList(c);
        System.out.println(d);
    }
    //<T>要放在返回值的前面
    public static <T> List<T> reverseList(List<T> list){
        ArrayList<T> newList = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            newList.add(list.get(i));
        }
        return newList;
    }
}
