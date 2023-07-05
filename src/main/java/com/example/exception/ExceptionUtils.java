package com.example.exception;

import lombok.experimental.UtilityClass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * @author PENGJINFEI533
 * @since 2019-7-31
 */
@UtilityClass
public class ExceptionUtils {

    public static Throwable getRealException(Throwable throwable) {
        if (throwable instanceof UndeclaredThrowableException) {
            return getRealException(((UndeclaredThrowableException) throwable));
        } else if (throwable instanceof InvocationTargetException) {
            return getRealException(((InvocationTargetException) throwable));
        }
        return throwable;
    }

    public static Throwable getRealException(UndeclaredThrowableException ex) {
        return getRealException(ex.getUndeclaredThrowable());
    }

    public static Throwable getRealException(InvocationTargetException ex) {
        return getRealException(ex.getTargetException());
    }
}
