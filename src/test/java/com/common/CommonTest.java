package com.common;

import cn.hutool.Hutool;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSON;
import com.example.util.NciUrlUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Liwei
 * @Date 2022/7/10 15:42
 */
public class CommonTest {
    @Test
    public void a1() {
        Integer i1 = 40;
        Integer i2 = new Integer(40);
        System.out.println(i1 == i2);
        // 字符串常量池中已存在字符串对象“abc”的引用
        String s1 = "abc";
        // 下面这段代码只会在堆中创建 1 个字符串对象“abc”
        String s2 = new String("abc");
        System.out.println(s1 == s2);
    }

    @Test
    public void mytest() {
// 在堆中创建字符串对象”Java“
// 将字符串对象”Java“的引用保存在字符串常量池中
        String s1 = "Java";
// 直接返回字符串常量池中字符串对象”Java“对应的引用
        String s2 = s1.intern();
// 会在堆中在单独创建一个字符串对象
        String s3 = new String("Java");
// 直接返回字符串常量池中字符串对象”Java“对应的引用
        String s4 = s3.intern();
// s1 和 s2 指向的是堆中的同一个对象
        System.out.println(s1 == s2); // true
// s3 和 s4 指向的是堆中不同的对象
        System.out.println(s3 == s4); // false
// s1 和 s4 指向的是堆中的同一个对象
        System.out.println(s1 == s4); //true

    }

    @Test
    public void mytest2() {
        String s = new String("1");
        s.intern();//调用此方法之前，字符串常量池中已经存在了"1"
        String s2 = "1";
        System.out.println(s == s2);//jdk6：false   jdk7/8：false

        /*
         1、s3变量记录的地址为：new String("11")
         2、经过上面的分析，我们已经知道执行完pos_1的代码，在堆中有了一个new String("11")
         这样的String对象。但是在字符串常量池中没有"11"
         3、接着执行s3.intern()，在字符串常量池中生成"11"
           3-1、在JDK6的版本中，字符串常量池还在永久代，所以直接在永久代生成"11",也就有了新的地址
           3-2、而在JDK7的后续版本中，字符串常量池被移动到了堆中，此时堆里已经有new String（"11"）了
           出于节省空间的目的，直接将堆中的那个字符串的引用地址储存在字符串常量池中。没错，字符串常量池
           中存的是new String（"11"）在堆中的地址
         4、所以在JDK7后续版本中，s3和s4指向的完全是同一个地址。
         */
        String s3 = new String("1") + new String("1");//pos_1
        s3.intern();

        String s4 = "11";//s4变量记录的地址：使用的是上一行代码代码执行时，在常量池中生成的"11"的地址
        System.out.println(s3 == s4);//jdk6：false  jdk7/8：true
    }

    @Test
    public void StringInternTest(){
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
    public void StringInternTest2(){
        String x = "ab";
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
    public void StringInternTest3(){
        String s1 = new String("ab");//执行完以后，会在字符串常量池中会生成"ab"
        s1.intern();
        String s2 = "ab";
        System.out.println(s1 == s2);//false
    }

    @Test
    public void mytest3() throws Exception {
        String original = "https://test-hcz-static.pingan.com.cn/umc-mall/battery/index.html#/orderDetail?orderId=ND23061512323989&esgCientId=P_BATTERY_YZT&from=uniOrder";
        MultiValueMap<String, String> queryParams = NciUrlUtils.parse(original).getQueryParams();
        System.out.println(queryParams);
        String var1 = NciUrlUtils.builder(original).replaceQuery(null).build().toString();
        System.out.println(var1);

        LinkedMultiValueMap<String, String> modifyMap = new LinkedMultiValueMap<>();
        BeanUtil.copyProperties(queryParams,modifyMap);
        modifyMap.remove("esgCientId");
        modifyMap.remove("from");
        System.out.println(modifyMap);

        String var3 = NciUrlUtils.builder(original).replaceQueryParams(modifyMap).build().toString();
        System.out.println(var3);


        String var4 = NciUrlUtils.standardize(original);
        System.out.println(var4);


    }



    @Test
    public void test4(){
        String original = "https://test-hcz-static.pingan.com.cn/umc-mall/battery/index.html#/orderDetail?orderId=ND23061512323989&esgCientId=P_BATTERY_YZT&from=uniOrder";

        String regex = "(?<=#)[^?#]+";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(original);

        if (matcher.find()) {
            String fragment = matcher.group();
            System.out.println("Fragment: " + fragment);
        }

        boolean b = this.isContainFragment(original);
        System.out.println(b);

        MultiValueMap<String, String> queryParams = NciUrlUtils.parse(original).getQueryParams();
        String orderId = queryParams.getFirst("orderId");
        System.out.println(orderId);
    }

    public boolean isContainFragment(String url){
        String regex = "(?<=#)[^?#]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return true;
        }
        return false;
    }


    @Test
    public void test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Integer> list = new ArrayList<>();

        list.add(12);
        //这里直接添加会报错
        // list.add("a");
        Class<? extends List> clazz = list.getClass();
        Method add = clazz.getDeclaredMethod("add", Object.class);
    //但是通过反射添加，是可以的
        add.invoke(list, "Nokia");

        System.out.println(list);

    }


    @Test
    public void parseSpecialJsonTest2(){
        String input = "{\"ret\":\"0\",\"msg\":\"\",\"requestId\":\"f185f6b025d38fa5\",\"data\":\"{\"responseCode\":\"0\",\"accessToken\":\"23e4ba4347224ebc8e69ad1a945d8ad6\"}\"}";
        String modify = input.replace("\"data\":\"", "\"data\":").replace("}\"}", "}}");
        String token = JSON.parseObject(modify).getJSONObject("data").getString("accessToken");
        System.out.println(token);

        String key = "0123456789ABHAEQ";
        String vi = "DYgjCEIMVrj2W9xN";
        System.out.println(key.length());
        System.out.println(vi.length());
    }

    @Test
    public void AESTest(){
        final String AES_KEY = "0CoJUm6Qyw8W8jud";
        // AES初始化向量为16位
        final String AES_IV = "3vR63quCNIQXfhXh";
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, AES_KEY.getBytes(), AES_IV.getBytes());

        String content = "18673130825";
        String encryptBase64 = aes.encryptBase64(content);
        System.out.println(encryptBase64);
        String decryptStr = aes.decryptStr(encryptBase64);
        System.out.println(decryptStr);

//        String encryptHex = aes.encryptHex(content);
//        System.out.println(encryptHex);
//
//        String decryptStr = aes.decryptStr(encryptHex);
//        System.out.println(decryptStr);

    }

}
