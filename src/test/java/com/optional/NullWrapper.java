package com.optional;

import java.util.Objects;
import java.util.function.Function;

/**
 * 工具类，用于简化空指针的探测工作
 * 核心思想：将可能为null的value包装成NullWrapper对象，那么调用nullWrapper.xxx()方法肯定就不会发生NullPointerException
 * 核心方法：map()
 * <p>
 * map()方法实际操作的是nullWrapper对象内部的value，将value映射为指定的值（比如下一级的字段）
 * 如果value为null，调用empty()方法，返回包装了null的NullWrapper对象
 * 如果value不为null，执行mapper.apply(value)得到结果并调用ofNullable(T newValue)方法，返回包装了newValue的NullWrapper对象
 *
 * @param <T>
 */
public final class NullWrapper<T> {

    /**
     * 配合{@link NullWrapper#empty()}使用，返回一个包装了null的NullWrapper
     * 主要是为了避免每次重复创建空的Wrapper！
     */
    private static final NullWrapper<?> EMPTY = new NullWrapper<>();

    /**
     * 实际值
     */
    private final T value;

    /**
     * 构造器，包装null
     */
    private NullWrapper() {
        this.value = null;
    }

    /**
     * 构造器，包装指定的【非空值】
     * 如果传入null会抛NullPointerException
     *
     * @param value
     */
    private NullWrapper(T value) {
        this.value = Objects.requireNonNull(value);
    }

    /**
     * 静态方法，返回一个包装了null的NullWrapper
     *
     * @param <T>
     * @return
     */
    public static <T> NullWrapper<T> empty() {
        @SuppressWarnings("unchecked")
        NullWrapper<T> t = (NullWrapper<T>) EMPTY;
        return t;
    }

    /**
     * 静态方法，包装指定的【非空值】
     * 如果传入null会抛异常NullPointerException
     *
     * @param value
     * @param <T>
     * @return
     */
    public static <T> NullWrapper<T> of(T value) {
        return new NullWrapper<>(value);
    }

    /**
     * 静态方法，包装指定值，允许null。当传入null时，会调用empty()方法返回EMPTY对象
     *
     * @param value
     * @param <T>
     * @return
     */
    public static <T> NullWrapper<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }

    /**
     * 是否有值（非null）
     *
     * @return
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * 核心方法
     * 调用者：NullWrapper对象，内部含有value，可能为null，也可能不为null
 	 * 如果value为null，调用empty()方法，返回包装了null的NullWrapper对象
 	 * 如果value不为null，执行mapper.apply(value)得到结果并调用ofNullable(T newValue)方法，返回包装了newValue的NullWrapper对象
     * 简而言之，每次调用map()，都有可能剥掉一层外壳，也意味着躲过了一次潜在的NullPointerException
     *
     * @param mapper
     * @param <U>
     * @return
     */
    public <U> NullWrapper<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent())
            return empty();
        else {
            return NullWrapper.ofNullable(mapper.apply(value));
        }
    }

    /**
     * 终端操作，当最终结果还是为null时，返回默认值other
     *
     * @param other
     * @return
     */
    public T orElse(T other) {
        return value != null ? value : other;
    }

    // ---------- 对Object方法重写，不用关注 -----------

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof NullWrapper)) {
            return false;
        }

        NullWrapper<?> other = (NullWrapper<?>) obj;
        return Objects.equals(value, other.value);
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value != null
                ? String.format("Optional[%s]", value)
                : "Optional.empty";
    }
}