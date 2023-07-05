package com.common;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson2.JSON;
import com.example.util.NciUrlUtils;
import com.google.common.collect.Lists;
import com.onjava.Tuple2;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
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
        String a=null;
        Objects.requireNonNull(a);
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




}
