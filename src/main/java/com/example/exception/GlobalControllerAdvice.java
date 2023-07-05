package com.example.exception;

import com.example.dto.ResultDTO;
import com.example.exception.BizException;
import com.example.exception.BizRuntimeException;
import com.example.exception.CodeMsgException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author PENGJINFEI533
 * @see org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
 * @since 2018/12/19
 */
@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {


    @Autowired
    @Setter
    private HandlerExceptionResolver handlerExceptionResolver;

    @ResponseBody
    @ExceptionHandler({BizException.class, BizRuntimeException.class})
    public ResultDTO handleCodeException(CodeMsgException e) {
        log.warn("CodeMsgException occurred. code:{}, msg:{}", e.getCode(), e.getMessage());
        return ResultDTO.fail(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResultDTO handleNotValidException(MethodArgumentNotValidException e) {
        return processBindingResult(e.getBindingResult());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResultDTO handleMethodArgumentTypeMisMatch(MethodArgumentTypeMismatchException e) {
        log.warn("请求参数格式错误:{}", e.getName());
        return ResultDTO.fail("请求参数格式错误");
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ResultDTO handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("非法参数异常:{}", ex);
        return ResultDTO.fail(ex.getLocalizedMessage());
    }

    @ResponseBody
    @ExceptionHandler(IllegalStateException.class)
    public ResultDTO handleIllegalStateException(IllegalStateException ex) {
        log.error("非法状态异常:{}", ex);
        return ResultDTO.fail(ex.getLocalizedMessage());
    }

    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResultDTO handleMissParam(MissingServletRequestParameterException e) {
        log.warn("缺少请求参数:{}", e.getParameterName());
        return ResultDTO.fail("缺少请求参数");
    }

    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultDTO handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String method = ex.getMethod();
        return ResultDTO.fail(String.format("不支持%s请求方式", method));
    }

    @ResponseBody
    @ExceptionHandler({BindException.class})
    public ResultDTO handleBindException(BindException e) {
        return processBindingResult(e.getBindingResult());
    }

    ResultDTO processBindingResult(BindingResult bindingResult) {
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        String str = allErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(","));
        return ResultDTO.fail(str);
    }

    @ExceptionHandler({UndeclaredThrowableException.class, InvocationTargetException.class})
    @ResponseBody
    public Object handleProxyException(Exception ex, HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) {
        Throwable realException = ExceptionUtils.getRealException(ex);
        if (realException instanceof Exception) {
            return handlerExceptionResolver.resolveException(request, response, handlerMethod, (Exception) realException);
        } else {
            return handleThrowable(realException);
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResultDTO handleMessageException(HttpMessageNotReadableException e) {
        log.warn("请求体格式错误:{}", e.getLocalizedMessage());
        return ResultDTO.fail("请求体格式错误");
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseBody
    public ResultDTO handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        final String headerName = e.getHeaderName();
        log.warn("缺少请求头{}", headerName);
        return ResultDTO.fail(String.format("缺少请求头%s", headerName));
    }


    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public Object handleThrowable(Throwable ex) {
        log.error("internal error occurred", ex);
        return ResultDTO.fail("网络异常，请稍后重试");
    }
}
