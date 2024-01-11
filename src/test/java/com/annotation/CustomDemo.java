package com.annotation;

/**
 * @author qiyu
 */
@CustomAnnotation(getValue = "annotation on class")
public class CustomDemo {

    @CustomAnnotation(getValue = "annotation on field")
    public String name;

    @CustomAnnotation(getValue = "annotation on method")
    public void hello() {}

    @CustomAnnotation() // 故意不指定getValue
    public void defaultMethod() {}


}