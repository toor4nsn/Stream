package com.stream;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.po.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.omg.CORBA.portable.IDLEntity;
import org.springframework.beans.BeanUtils;
import sun.security.provider.MD5;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author Liwei
 * @Date 2021/7/3 13:41
 */
public class demo {
    @Test
    public void test1() {
        Comparator<Integer> com1 = (x, y) -> Integer.compare(x, y);
        System.out.println(com1.compare(1, 2));
        Comparator<Integer> com2 = Integer::compare;
        System.out.println(com2.compare(2, 1));
        System.out.println(com2.compare(1,1));

        BiPredicate<String,String> bp= String::equals;

        Supplier<Student> sup= Student::new;
        System.out.println(sup.get());
        Function<Integer,Student> fuc= Student::new;
        Student apply = fuc.apply(1);
        System.out.println(apply);
    }

    @Test
    public void test2(){
        List<Integer> list = Stream.of(1, 2, 3, 4, 5).collect(Collectors.toList());
        Optional<Integer> first = list.stream().map(x -> x * x).filter(x -> x % 3 == 0).findFirst();
        first.ifPresent(System.out::println);
    }

    @Test
    public void test3(){
        Integer reduce = Stream.of(1, 2, 3, 4, 5).reduce(1, (a, b) -> a * b);
        System.out.println(reduce);
        Optional<Integer> max = Stream.of(1, 2, 3, 4, 5).reduce(Integer::max);
        max.ifPresent(x-> System.out.println(x));

        Long aLong = Stream.of(1, 2, 3, 4, 5).collect(Collectors.reducing(0L, e -> 1L, Long::sum));
        System.out.println(aLong);

    }

    @Test
    public void cal(){
        ArrayList<Integer> list1 = Lists.newArrayList(1, 2, 3, 4);
        ArrayList<String> list2 = Lists.newArrayList("a", "b", "c", "d");

        JSONObject j1 = new JSONObject();
        j1.put("list1",list1);
        JSONObject j2 = new JSONObject();
        j2.put("list2",list2);
        JSONObject combined = new JSONObject();

        combined.put("list1", j1.getJSONArray("list1"));
        combined.put("list2", j2.getJSONArray("list2"));


        System.out.println(combined);

        System.out.println(j1);
        System.out.println(JSON.toJSONString(list1));


    }

    @Test
    public void t1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        HashMap<String, String> m1 = new HashMap<>();
        m1.put("a","aa");
        m1.put("b","bb");
        m1.put("c","cc");
        m1.put("d","dd");
        HashMap<String, String> m2 = new HashMap<>();
        for (Map.Entry<String, String> entry : m1.entrySet()) {
            m2.put(entry.getValue(),entry.getKey());
        }
        System.out.println(m2);

        Map<String, String> collect = m1.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        System.out.println(collect);

        ArrayList<Integer> list = new ArrayList<>();
        Class<? extends List> aClass = list.getClass();
        Method add = aClass.getDeclaredMethod("add", Object.class);
        add.invoke(list,"Nokia");
        System.out.println(list);
    }

    @Test
    public void t2(){
        String appId = "op58848b404206a14";
        String appSecret = "ZJfRe8WAvKp7L50sajQQf7blmY48SNkJ";
        String eventId = "156";
        String openid="123456";
        String identity="123456";

        Map<String, String> params = new HashMap<>();
        params.put("event_id", eventId);
        params.put("identity", identity);
        params.put("app_id", appId);
        params.put("openid", openid);

        String jsonStr = JSON.toJSONString(params);
        System.out.println(jsonStr);
        String encryptStr = jsonStr + "&app_secret=" + appSecret;
        System.out.println(encryptStr);
        String sign = SecureUtil.md5(encryptStr);
        System.out.println(sign);

    }

    @Test
    public void t3(){
        ProductInfo info1 = new ProductInfo();
        info1.setProductId("01");
        info1.setProductCode("MP001");
        info1.setProductName("健康险");
        ProductInfo info2 = new ProductInfo();
        info2.setProductId("02");
        info2.setProductCode("MP002");
        info2.setProductName("医疗险");
        ProductInfo info3 = new ProductInfo();
        info3.setProductId("03");
        info3.setProductCode("MP003");
        info3.setProductName("意外险");
        ProductInfo info4 = new ProductInfo();
        info4.setProductId("04");
        info4.setProductCode("MP004");
        info4.setProductName("宠物险");
        ArrayList<ProductInfo> productInfos = Lists.newArrayList(info1, info2, info3, info4);

        Product product = new Product();
        product.setId("1");
        product.setMediaSource("MTLY:001");
        product.setInfoList(productInfos);


        ProductInfo info6 = new ProductInfo();
        info6.setProductId("06");
        info6.setProductCode("MP006");
        info6.setProductName("健康险MP006");
        ProductInfo info7 = new ProductInfo();
        info7.setProductId("07");
        info7.setProductCode("MP007");
        info7.setProductName("医疗险MP007");
        ProductInfo info8 = new ProductInfo();
        info8.setProductId("08");
        info8.setProductCode("MP008");
        info8.setProductName("意外险MP008");
        ProductInfo info5 = new ProductInfo();

        info5.setProductId("05");
        info5.setProductCode("MP005");
        info5.setProductName("宠物险MP005");
        ArrayList<ProductInfo> productInfos2 = Lists.newArrayList(info6, info7, info8, info5);

        Product product2 = new Product();
        product2.setId("2");
        product2.setMediaSource("MTLY:002");
        product2.setInfoList(productInfos2);

        ArrayList<Product> list = Lists.newArrayList(product, product2);

        Map<String, List<String>> map = list.stream().collect(Collectors.toMap(Product::getMediaSource,
                item -> item.getInfoList().stream().map(ProductInfo::getProductCode).collect(Collectors.toList())));

/*        HashMap<String, List<String>> hashMap=new HashMap<>();
        for (Product item : list) {
            String mediaSource = item.getMediaSource();
            List<String> productCodeList = item.getInfoList().stream().map(ProductInfo::getProductCode).collect(Collectors.toList());
            hashMap.put(mediaSource,productCodeList);
        }*/


        System.out.println(map);

        Map<String, List<Product>> collect = list.stream().collect(Collectors.groupingBy(Product::getMediaSource));

        HashMap<String, List<String>> resultMap = Maps.newHashMap();
        for (Map.Entry<String, List<Product>> entry : collect.entrySet()) {
            String key = entry.getKey();
            //method1
            List<String> productCodeList = entry.getValue()
                    .stream()
                    .map(Product::getInfoList)
                    .flatMap(item -> item.stream().map(ProductInfo::getProductCode))
                    .collect(Collectors.toList());
            //method2
            List<String> productCodeList2 = entry.getValue().stream().map(item -> item.getInfoList().stream().map(ProductInfo::getProductCode).collect(Collectors.toList())).flatMap(Collection::stream).collect(Collectors.toList());
            resultMap.put(key,productCodeList);
        }
        System.out.println(resultMap);
    }

    @Test
    public void gsonTest(){
        Gson gson = new Gson();
        String jsonArray = "[\"Android\",\"Java\",\"PHP\"]";
        String[] strings = gson.fromJson(jsonArray, String[].class);
        List<String> stringList = gson.fromJson(jsonArray, new TypeToken<List<String>>() {}.getType());
        System.out.println(new Gson().toJson(stringList));
    }

    @Test
    public void fastJsonTest(){
        String a="[\"Android\",\"Java\",\"PHP\"]";
        List<String> list = JSON.parseArray(a, String.class);
        System.out.println(list);
    }
    @Test
    public void hashMapTest(){
        HashMap<String, String> map = new HashMap<>();
        map.put("a","toor");
        String b = map.compute("b", (k, v) -> "result");
        System.out.println("b:"+ b);
        System.out.println(map);

        String s = map.putIfAbsent("b", "newResult");
        System.out.println("s:"+s);
        System.out.println(map);
    }

    @Test
    public void bigDecimalTest(){
        Product p1 = new Product();
        p1.setPrice(new BigDecimal("1.123"));
        p1.setId("1");
        p1.setMediaSource("m1");

        Product p2 = new Product();
        p2.setPrice(new BigDecimal("1.456"));
        p2.setId("2");
        p2.setMediaSource("m2");

        ArrayList<Product> list = Lists.newArrayList(p1, p2);
        BigDecimal result = list.stream().map(Product::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println(result);
    }

    @Test
    public void testStringTable(){
        String s1 = "javaEEhadoop";
        String s2 = "javaEE";//变量
        String s3 = s2 + "hadoop";
        System.out.println(s1 == s3);//false
        final String s4 = "javaEE";//常量
        String s5 = s4 + "hadoop";
        System.out.println(s1 == s5);//true

    }

    @Test
    public void testString2(){
        String s = new String("1");
        s.intern();//调用此方法之前，字符串常量池中已经存在了"1"
        String s2 = "1";
        System.out.println(s == s2);

        String s3 = new String("1") + new String("1");//pos_1  new String("11")
        s3.intern();


        String s4 = "11";//s4变量记录的地址：使用的是上一行代码代码执行时，在常量池中生成的"11"的地址
        System.out.println(s3 == s4);
    }
    @Test
    public void testString3(){
        //执行完下一行代码以后，字符串常量池中，是否存在"11"呢？答案：不存在！！
        String s3 = new String("1") + new String("1");//new String("11")
        //在字符串常量池中生成对象"11"，代码顺序换一下，实打实的在字符串常量池里有一个"11"对象
        String s4 = "11";
        String s5 = s3.intern();

        // s3 是堆中的 "ab" ，s4 是字符串常量池中的 "ab"
        System.out.println(s3 == s4);//false

        // s5 是从字符串常量池中取回来的引用，当然和 s4 相等
        System.out.println(s5 == s4);//true
    }
    @Test
    public void testString4(){
//        String x = "ab";
        String s = new String("a") + new String("b");//new String("ab")
        //在上一行代码执行完以后，字符串常量池中并没有"ab"
		/*
		1、jdk6中：在字符串常量池（此时在永久代）中创建一个字符串"ab"
        2、jdk8中：字符串常量池（此时在堆中）中没有创建字符串"ab",而是创建一个引用，指向new String("ab")，		  将此引用返回
        3、详解看上面
		*/
        String s2 = s.intern();

        System.out.println(s2 == "ab");//jdk6:true  jdk8:true
        System.out.println(s == "ab");//jdk6:false  jdk8:true
    }
    @Test
    public void testString5(){
//        String s1=new String("ab");
        String s1= new String("a") + new String("b");
        s1.intern();
        String s2="ab";
        System.out.println(s1==s2);
    }
    @Test
    public void testList(){
        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6);
        Optional<Integer> max = list.stream().max(Comparator.comparingInt(Integer::intValue));
        if (max.isPresent()){
            System.out.println(max.get());
        }else {
            System.out.println("null");
        }
        list.replaceAll(item -> item * item);
        System.out.println(list);
    }

    @Test
    public void testMAP(){
        HashMap<String, String> map = Maps.newHashMap();
        map.put("a", "5");
        map.put("b", "4");
        map.put("c", "3");
        map.put("d", "2");
        map.put("e", "1");
        map.forEach((key, value) -> {
            System.out.println("key：" + key + "  " + "value:" + value);
        });
        map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(System.out::println);
        map.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(System.out::println);

        String result = map.getOrDefault("f", "defaultValue");
        System.out.println(result);

        //computeIfAbsent——如果指定的键没有对应的值（没有该键或者该键对应的值是空），那么使用该键计算新的值，并将其添加到Map中
        //computeIfAbsent的一个应用场景是缓存信息
        HashMap<String, List<String>> map2 = new HashMap<>();
        boolean add = map2.computeIfAbsent("movie", value -> new ArrayList<>()).add("new item");
        System.out.println(add);
        System.out.println(map2);

        map.entrySet().removeIf(item-> Integer.parseInt(item.getValue()) < 3);
        System.out.println(map);
    }

}
