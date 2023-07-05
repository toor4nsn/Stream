package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;



/**
 * @author PENGJINFEI533
 * @since 2018/12/19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultDTO<T> implements Serializable {

    private String reqId;
    private String msg;
    private Integer code;
    private T data;

    private static final ConcurrentHashMap<Integer, Class> CODE_MAP = new ConcurrentHashMap<>();
    private static final String SUCCESS_MSG = "操作成功";

    public static Integer register(Integer code, Class<?> aClass) {
        Class previous = CODE_MAP.putIfAbsent(code, aClass);
        if (previous != null) {
            throw new IllegalArgumentException(String.format("Registration of code %d by class:%s is failed,which is already registered by class:%s", code, aClass.getName(), previous.getName()));
        }
        return code;
    }

    static {
        register(0, ResultDTO.class);
        register(-1, ResultDTO.class);
    }

    public static final Integer SUCC_CODE = 0;

    public static final Integer UNKOWN_CODE = -1;

    public static ResultDTO success() {
        return ResultDTO.builder().code(SUCC_CODE).msg(SUCCESS_MSG).build();
    }

    public static ResultDTO success(int code, String msg) {
        return ResultDTO.builder().code(code).msg(msg).build();
    }

    public static <T> ResultDTO<T> success(int code, String msg, T t) {
        return ResultDTO.<T>builder().code(code).msg(msg).data(t).build();
    }

    public static ResultDTO unkown() {
        return ResultDTO.builder().code(UNKOWN_CODE).msg("").build();
    }

    public static <T> ResultDTO<T> success(T t) {
        return ResultDTO.<T>builder().code(SUCC_CODE).msg(SUCCESS_MSG).data(t).build();
    }

    public static ResultDTO fail(int code, String msg) {
        return ResultDTO.builder().code(code).msg(msg).build();
    }

    public static ResultDTO fail(String message) {
        return fail(UNKOWN_CODE, message);
    }

    public static <T> ResultDTO<T> fail(int code, String msg, T t) {
        return ResultDTO.<T>builder().code(code).msg(msg).data(t).build();
    }

}
