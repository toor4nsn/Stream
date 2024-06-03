package com.example.enums;

import java.math.BigDecimal;

public enum MemberEnum {
    // ---------- 把这几个枚举当做本应该在外面实现的MemberEnum子类，不要看成MemberEnum内部的 ----------
    GOLD_MEMBER(1, "黄金会员") {
        @Override
        public BigDecimal calculateFinalPrice(BigDecimal originPrice) {
            return originPrice.multiply(new BigDecimal(("0.6")));
        }
    },
    SILVER_MEMBER(2, "白银会员") {
        @Override
        public BigDecimal calculateFinalPrice(BigDecimal originPrice) {
            return originPrice.multiply(new BigDecimal(("0.7")));
        }
    },
    BRONZE_MEMBER(3, "青铜会员") {
        @Override
        public BigDecimal calculateFinalPrice(BigDecimal originPrice) {
            return originPrice.multiply(new BigDecimal(("0.8")));
        }
    },
    ;

    // ---------- 下面才是MemberEnum类的定义 ---------
    private final Integer type;
    private final String desc;

    MemberEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * 在枚举类中定义抽象方法，强制子类实现
     * 定义抽象方法，
     * 留个子类实现
     *
     * @param originPrice
     * @return
     */
    public abstract BigDecimal calculateFinalPrice(BigDecimal originPrice);



    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static MemberEnum getEnumByType(Integer type) {
        MemberEnum[] values = MemberEnum.values();
        for (MemberEnum memberEnum : values) {
            if (memberEnum.getType().equals(type)) {
                return memberEnum;
            }
        }
        throw new IllegalArgumentException("Invalid Enum type:" + type);
    }
}