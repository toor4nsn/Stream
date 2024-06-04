package com.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatusEnum { // 这个才是枚举类

    // 这两个看起来是“字段”的玩意儿，其实是对象（枚举对象）
    DELETE(-1, "删除"),
    DEFAULT(0, "默认");

    private final Integer status;
    private final String desc;

}
