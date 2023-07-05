package com.optional;

import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.function.Function;


public final class MiniNullWrapper<T> {

    /**
     * 实际值
     */
    private final T value;

    /**
     * 无参构造，默认包装null
     */
    private MiniNullWrapper() {
        this.value = null;
    }

    /**
     * 有参构造器，用来包装外部传入的value
     *
     * @param value
     */
    private MiniNullWrapper(T value) {
        this.value = value;
    }

    /**
     * 静态方法，返回一个包装了null的Wrapper
     *
     * @param <T>
     * @return
     */
    public static <T> MiniNullWrapper<T> empty() {
        // 调用无参构造，返回包装了null的Wrapper
        System.out.println("由于value为null，直接返回包装了null的Wrapper，让流程继续往下");
        return new MiniNullWrapper<>();
    }

    /**
     * 静态方法，返回一个包装了value的Wrapper（value可能为null）
     *
     * @param value
     * @param <T>
     * @return
     */
    public static <T> MiniNullWrapper<T> ofNullable(T value) {
        // 调用有参构造，返回包装了value的Wrapper
        return new MiniNullWrapper<>(value);
    }

    /**
     * 核心方法：
     * 1.如果value为null，直接返回空的Wrapper
     * 2.如果value不为null，则使用mapper对value进行处理，往下剥一层（这是关键，一有机会就要往下剥一层，否则就是原地踏步）
     *
     * @param mapper
     * @param <U>
     * @return
     */
    public <U> MiniNullWrapper<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (value == null)
            // 按上面说的，如果value为null，我都不处理了。但为了调用者拿到返回值后不会发生空指针，需要用Wrapper包装一下
            return MiniNullWrapper.empty();
        else {
            /*
             * value不为null，那么就要想尽办法将它剥去一层皮。
             * 由于此时value不为null，即使mapper的apply方法要做的操作是 value.getXxx()/value.setXxx()，都不会空指针
             * mapper.apply(value)处理后的结果继续用Wrapper包装，此时【新的wrapper里的value】是处理后的数据（下一层）
             * */
            return MiniNullWrapper.ofNullable(mapper.apply(value));
        }
    }

    /**
     * 终端操作，决定勇敢一次。当你做好面对外面的世界时，就要卸下伪装：直接把value丢出去。
     * 但为了不祸害别人，给个备选值：other。当你确实是null时，返回other。
     *
     * @param other
     * @return
     */
    public T orElse(T other) {
        return value != null ? value : other;
    }

    // -------- 测试方法 ---------

    public static void main(String[] args) {
        // 全部不为null
        Son sonNotNull = new Son("大头儿子");
        Father fatherNotNull = new Father();
        fatherNotNull.setSon(sonNotNull);
        GrandPa grandPaNotNull = new GrandPa();
        grandPaNotNull.setFather(fatherNotNull);
        // 处理grandPa，观察map()中的处理方法有没有被调用
        String sonName1 = MiniNullWrapper.ofNullable(grandPaNotNull)
                .map(grandPa -> grandPa.getFather())
                .map(father -> father.getSon())
                .map(son -> son.getName())
                .orElse("没得到儿子的名字");

         //全部为null
        GrandPa grandPaNull = new GrandPa();
        grandPaNull.setFather(null);
        // 处理grandPa，观察map()，你会发现，从grandPa取出father后，由于发现是null，所以father->father.getSon()不会执行，避免了空指针
//        String sonName2 = MiniNullWrapper.ofNullable(grandPaNull)
//                .map(grandPa -> grandPa.getFather())
//                .map(father -> father.getSon())
//                .map(son -> son.getName())
//                .orElse("没得到儿子的名字");
    }

    // ---- 没啥实质内容，就是几个简单的类，我在getter方法中打印了一些信息 ----
    static class GrandPa {
        private Father father;

        public Father getFather() {
            System.out.println("GrandPa#getFather被调用了");
            return father;
        }

        public void setFather(Father father) {
            this.father = father;
        }
    }

    static class Father {
        private Son son;

        public Son getSon() {
            System.out.println("Father#getSon被调用了");
            return son;
        }

        public void setSon(Son son) {
            this.son = son;
        }
    }

    @AllArgsConstructor
    static class Son {
        private String name;

        public String getName() {
            System.out.println("Son#getName被调用了");
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}