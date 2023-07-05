package com.stream;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import javafx.scene.media.VideoTrack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
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
    public void testDeepCopy() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Student s = new Student();
        s.setAge(28);
        s.setName("toor");
        s.setDepartment("Math");
        ArrayList<Student> list = Lists.newArrayList(s);
        ArrayList<Student> copy = ObjectUtil.cloneByStream(list);

        System.out.println(copy);

//        System.out.println("9223372036854775807".length());

        Class<?> aClass = Class.forName("com.example.po.Person");
        Person person = (Person) aClass.newInstance();

    }

    @Test
    public void jsonArrayTest(){
        String input ="{\n" +
                "    \"message\": \"OK\",\n" +
                "    \"code\": 0,\n" +
                "    \"data\": {\n" +
                "        \"list\": [\n" +
                "            {\n" +
                "                \"advertiser_id\": 1643827007904776,\n" +
                "                \"advertiser_name\": \"中国平安保险(集团)股份有限公司-平安车险-04\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"advertiser_id\": 1643828239022093,\n" +
                "                \"advertiser_name\": \"中国平安保险(集团)股份有限公司-平安车险-05\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"advertiser_id\": 1671551817051143,\n" +
                "                \"advertiser_name\": \"中国平安财产保险股份有限公司-平安车险-01-头条搜索\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"advertiser_id\": 1686854008579079,\n" +
                "                \"advertiser_name\": \"中国平安财产保险股份有限公司-平安车险-06-搜索\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"advertiser_id\": 1689823491520520,\n" +
                "                \"advertiser_name\": \"中国平安财产保险股份有限公司-平安车险-07-抖音\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"advertiser_id\": 1689825338059783,\n" +
                "                \"advertiser_name\": \"中国平安财产保险股份有限公司-平安车险-08-抖音\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"advertiser_id\": 1689825338525703,\n" +
                "                \"advertiser_name\": \"中国平安财产保险股份有限公司-平安车险-09-搜索\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"advertiser_id\": 1689825338937351,\n" +
                "                \"advertiser_name\": \"中国平安财产保险股份有限公司-平安车险-10-今日头条\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"advertiser_id\": 1700966466635789,\n" +
                "                \"advertiser_name\": \"中国平安财产保险股份有限公司-平安车险-011-抖音\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"advertiser_id\": 1700966467070990,\n" +
                "                \"advertiser_name\": \"中国平安财产保险股份有限公司-平安车险-012-搜索\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"advertiser_id\": 1700966467517448,\n" +
                "                \"advertiser_name\": \"中国平安财产保险股份有限公司-平安车险-013-抖音\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"advertiser_id\": 1700966468042823,\n" +
                "                \"advertiser_name\": \"中国平安财产保险股份有限公司-平安车险-014-抖音\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"advertiser_id\": 1700966468602894,\n" +
                "                \"advertiser_name\": \"中国平安财产保险股份有限公司-平安新能源车险-015户\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"advertiser_id\": 1733603375555598,\n" +
                "                \"advertiser_name\": \"中国平安财产保险股份有限公司-平安新能源车险-016-抖音\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"advertiser_id\": 1733603376547847,\n" +
                "                \"advertiser_name\": \"中国平安财产保险股份有限公司-平安车险-017-抖音\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"advertiser_id\": 1733603377507406,\n" +
                "                \"advertiser_name\": \"中国平安财产保险股份有限公司-平安车险-018-抖音\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"advertiser_id\": 1733603378348040,\n" +
                "                \"advertiser_name\": \"中国平安财产保险股份有限公司-平安车险-019-抖音\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"advertiser_id\": 1733603379450893,\n" +
                "                \"advertiser_name\": \"中国平安财产保险股份有限公司-平安车险-020-今日头条\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"advertiser_id\": 3680784748649022,\n" +
                "                \"advertiser_name\": \"中国平安保险(集团)股份有限公司-平安车险-03-今日头条\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"advertiser_id\": 3979851911935667,\n" +
                "                \"advertiser_name\": \"中国平安保险(集团)股份有限公司-平安车险-02-抖音\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"request_id\": \"2022120817005048EA492C5CE6473C977C\"\n" +
                "}";
        JSONObject jsonObject = JSONObject.parseObject(input);
        List<String> collect = jsonObject.getJSONObject("data").getJSONArray("list").stream()
                .map(item ->JSONObject.parseObject(JSON.toJSONString(item)).getString("advertiser_id")).collect(Collectors.toList());

//        ArrayList<String> res = new ArrayList<>();
//        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
//        for (int i = 0; i < jsonArray.size(); i++) {
//            String advertiser_id = jsonArray.getJSONObject(i).getString("advertiser_id");
//            res.add(advertiser_id);
//        }

        System.out.println(collect);
    }


    @Test
    public void mytest(){
        String s = "7074806092097929253";
        System.out.println(Long.valueOf(s));
    }
    
}
