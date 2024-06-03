package com.example.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlatformEnum {
    TAOBAO(1, "taobao", "http://xxx"),
    PDD(2,"pdd", "http://xxx");

    private final Integer code;
    private final String sourceKey;
    private final String iconUrl;

    public static PlatformEnum getByCode(Integer code) {
        for (PlatformEnum platformEnum : PlatformEnum.values()) {
            if (platformEnum.getCode().equals(code)) {
                return platformEnum;
            }
        }
        return null;
    }
}