package com.example.service;


import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.*;

public class CouponTest {

    public static void main(String[] args) {
        List<CouponResponse> coupons = getCoupons();

        // TODO 对优惠券统计数量
/*
        System.out.println(JSON.toJSONString(getCoupons()));
        Map<Long, List<CouponResponse>> step1 = getCoupons().stream().collect(Collectors.groupingBy(CouponResponse::getId, Collectors.toList()));
        System.out.println(JSON.toJSONString(step1));
        ArrayList<CouponInfo> couponInfo = Lists.newArrayList();
        Set<Map.Entry<Long, List<CouponResponse>>> entries = step1.entrySet();
        for (Map.Entry<Long, List<CouponResponse>> entry : entries) {
            CouponInfo info = new CouponInfo();

            List<CouponResponse> value = entry.getValue();
            if (value.size()>1){
                info.setNum(value.size());
            }else {
                info.setNum(1);
            }
            info.setId(value.get(0).getId());
            info.setName(value.get(0).getName());

            info.setCondition(value.get(0).condition);
            info.setDenominations(value.get(0).denominations);
            couponInfo.add(info);
        }

        System.out.println(JSON.toJSONString(couponInfo));*/

        System.out.println("==================");

/*        Map<Long, CouponInfo> couponInfoMap = coupons.stream().collect(Collectors.toMap(
                CouponResponse::getId, // 以id为key去重

                couponResponse -> {    // CouponResponse转CouponInfo
                    CouponInfo couponInfo = new CouponInfo();
                    couponInfo.setId(couponResponse.getId());
                    couponInfo.setName(couponResponse.getName());
                    couponInfo.setNum(1);
                    couponInfo.setCondition(couponResponse.getCondition());
                    couponInfo.setDenominations(couponResponse.getDenominations());
                    return couponInfo;
                },

                (pre, next) -> {       // 相同id的优惠券，保留前者，并给前者num +1
                    pre.setNum(pre.getNum() + 1);
                    return pre;
                })
        );

        System.out.println(JSON.toJSONString(couponInfoMap));*/

        System.out.println("=======================================");
        Map<Long, CouponInfo> couponInfoMap = new HashMap<>();
        for (CouponResponse coupon : getCoupons()) {
            CouponInfo couponInfo = couponInfoMap.getOrDefault(coupon.getId(), new CouponInfo());

            if (couponInfo.getNum() != null) {
                couponInfo.setNum(couponInfo.getNum() + 1);
                continue;
            }/*else {
                couponInfo.setNum(1);
            }*/

            BeanUtils.copyProperties(coupon, couponInfo);

            couponInfoMap.put(coupon.getId(), couponInfo);
        }
        System.out.println(JSON.toJSONString(couponInfoMap.values()));

    }

    private static List<CouponResponse> getCoupons() {
        return Lists.newArrayList(
                new CouponResponse(1L, "满5减4", 500L, 400L),
                new CouponResponse(1L, "满5减4", 500L, 400L),
                new CouponResponse(2L, "满10减9", 1000L, 900L),
                new CouponResponse(3L, "满60减50", 6000L, 5000L)
        );
    }

    @Data
    @AllArgsConstructor
    static class CouponResponse {
        private Long id;
        private String name;
        private Long condition;
        private Long denominations;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CouponInfo {
        private Long id;
        private String name;
        private Integer num;
        private Long condition;
        private Long denominations;
    }
}