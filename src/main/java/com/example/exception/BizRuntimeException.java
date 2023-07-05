package com.example.exception;

import lombok.Getter;

public class BizRuntimeException extends RuntimeException implements CodeMsgException {

    @Getter
    private Integer code;

    public BizRuntimeException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public BizRuntimeException(CodeMsg codeMsg) {
        super(codeMsg.getMsg());
        this.code = codeMsg.getCode();
    }

    public BizRuntimeException(CodeMsg codeMsg, Object... args) {
        super(String.format(codeMsg.getMsg(), args));
        this.code = codeMsg.getCode();
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}