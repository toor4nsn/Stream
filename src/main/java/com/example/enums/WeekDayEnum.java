package com.example.enums;

import java.util.HashMap;
import java.util.Map;

public enum WeekDayEnum {
    MONDAY(1, "星期一"),
    TUESDAY(2, "星期二"),
    WEDNESDAY(3, "星期三"),
    THURSDAY(4, "星期四"),
    FRIDAY(5, "星期五"),
    SATURDAY(6, "星期六"),
    SUNDAY(7, "星期日");

    private final Integer code;
    private final String desc;

    WeekDayEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public static WeekDayEnum getEnumByCode(Integer code) {
        WeekDayEnum[] values = WeekDayEnum.values();
        for (WeekDayEnum weekDayEnum : values) {
            if (weekDayEnum.getCode().equals(code)) {
                return weekDayEnum;
            }
        }
        return null;
    }


    private static final Map<Integer,WeekDayEnum> codeCache = new HashMap<>();
    static {
        for (WeekDayEnum weekDayEnum : WeekDayEnum.values()) {
            codeCache.put(weekDayEnum.code, weekDayEnum);
        }
    }
    public static WeekDayEnum getEnumByCode2(Integer code) {
        WeekDayEnum weekDayEnum = codeCache.get(code);
        if (weekDayEnum != null) {
            return weekDayEnum;
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }




}
