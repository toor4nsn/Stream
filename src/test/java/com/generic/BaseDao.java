package com.generic;

/**
 * 一个类拥有多种同类型方法时使用泛型类，
 * 一个方法处理多种类型时使用泛型方法。
 * @param <T>
 */
class BaseDao<T> {
    /**
     * 泛型类的方法
     * @param t
     * @return
     */
    public T get(T t){
        return t;
    }

    /**
     * 泛型方法，无返回值，所以是void。<E>出现在返回值前，表示声明E变量
     * @param e
     * @param <E>
     */
    public <E> void methodWithoutReturn(E e) {
        
    }

    /**
     * 泛型方法，有返回值。入参和返回值都是V。注意，即使这个方法也用E，也和上面的E不是同一个
     * @param v
     * @param <V>
     * @return
     */
    public <V> V methodWithReturn(V v) {
        return v;
    }
}