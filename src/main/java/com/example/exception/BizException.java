package com.example.exception;

import lombok.Getter;

public class BizException extends Exception implements CodeMsgException{

    @Getter
    private Integer code;

    public BizException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public BizException(CodeMsg codeMsg) {
        super(codeMsg.getMsg());
        this.code = codeMsg.getCode();
    }

    public BizException(CodeMsg codeMsg, Object... args) {
        super(String.format(codeMsg.getMsg(),args));
        this.code = codeMsg.getCode();
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}