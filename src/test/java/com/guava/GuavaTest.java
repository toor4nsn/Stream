package com.guava;

import com.example.po.Trader;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.*;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * Guava
 */
@Slf4j
public class GuavaTest {
    @Test
    public void HashBasedTableTest(){
        //guava中的Table允许一个value存在两个key
        Table<String,String,Integer> table= HashBasedTable.create();
        //存放元素
        table.put("Hydra", "Jan", 20);
        table.put("Hydra", "Feb", 28);
        table.put("Trunks", "Jan", 28);
        table.put("Trunks", "Feb", 16);

        //取出元素
        Integer Hydra = table.get("Hydra", "Feb");
        System.out.println(Hydra);
        Integer Trunks = table.get("Trunks", "Jan");
        System.out.println(Trunks);

        //rowKey或columnKey的集合
        Set<String> rowKeys = table.rowKeySet();
        Set<String> columnKeys = table.columnKeySet();
        //value集合
        Collection<Integer> values = table.values();
        log.info("rowKeys:{}",rowKeys);
        log.info("columnKeys:{}",columnKeys);
        log.info("values:{}",values);

        Map<String, Map<String, Integer>> rowMap = table.rowMap();
        Map<String, Map<String, Integer>> columnMap = table.columnMap();
        log.info("rowMap:{}",rowMap);
        log.info("columnMap:{}",columnMap);
    }

    @Test
    public void HashBiMapTest(){
        HashBiMap<String, String> biMap = HashBiMap.create();
        biMap.put("Hydra","Programmer");
        biMap.put("Tony","IronMan");
        biMap.put("Thanos","Titan");
        //使用key获取value
        System.out.println(biMap.get("Tony"));
        BiMap<String, String> inverse = biMap.inverse();
        //使用value获取key
        System.out.println(inverse.get("Titan"));
        //用inverse方法反转了原来BiMap的键值映射，但是这个反转后的BiMap并不是一个新的对象，
        // 它实现了一种视图的关联，所以对反转后的BiMap执行的所有操作会作用于原先的BiMap上
        //inverse.put("IronMan","Stark");
        //System.out.println(biMap);

        //由于BiMap的value是不允许重复的，方法返回的是没有重复的Set，而不是普通Collection：
        Set<String> values = biMap.values();
        System.out.println(values);
    }

    @Test
    public void MultimapTest(){
        //HashMap<K, ArrayList<V>>
        Multimap<String, Integer> multimap = ArrayListMultimap.create();
        multimap.put("day",1);
        multimap.put("day",2);
        multimap.put("day",8);
        multimap.put("day",8);
        multimap.put("day",8);
        multimap.put("month",3);
        log.info("multimap:{}",multimap);
        Collection<Integer> dayList = multimap.get("day");
        System.out.println(dayList);
        Collection<Integer> yearList = multimap.get("year");
        System.out.println(yearList);

        //Map<K, Set<V>>
        HashMultimap<String, String> hashMultimap = HashMultimap.create();
        hashMultimap.put("communication","Nokia");
        hashMultimap.put("communication","Huawei");
        hashMultimap.put("communication","ZTE");
        hashMultimap.put("communication","Ericsson");
        hashMultimap.put("communication","Ericsson");
        Set<String> res = hashMultimap.get("communication");
        System.out.println(res);

    }

    @Test
    public  void RangeMapTest(){
        RangeMap<Integer, String> rangeMap = TreeRangeMap.create();
        rangeMap.put(Range.closedOpen(0,60),"fail");
        rangeMap.put(Range.closed(60,90),"satisfactory");
        rangeMap.put(Range.openClosed(90,100),"excellent");
        System.out.println(rangeMap.get(59));
        System.out.println(rangeMap.get(60));
        System.out.println(rangeMap.get(90));
        System.out.println(rangeMap.get(91));

    }

    @Test
    public void ClassToInstanceMapTest(){
        ClassToInstanceMap<Object> instanceMap = MutableClassToInstanceMap.create();
        Trader trader = new Trader();
        trader.setName("Nokia");
        trader.setCity("Finland");
        instanceMap.putInstance(Trader.class,trader);
        Trader trader2  = instanceMap.getInstance(Trader.class);
        System.out.println(trader==trader2);
        System.out.println(trader2);
    }

    @Test
    public void collectionTest(){
        // 普通Collection的创建
        List<String> list = Lists.newArrayList();
        Set<String> set = Sets.newHashSet();
        Map<String, String> map = Maps.newHashMap();

        // 不变Collection的创建
        ImmutableList<String> iList = ImmutableList.of("a", "b", "c");
        ImmutableSet<String> iSet = ImmutableSet.of("e1", "e2");
        ImmutableMap<String, String> iMap = ImmutableMap.of("k1", "v1", "k2", "v2");
        System.out.println(iMap);
        //iMap.put("k3","v3");

        //Set 并集
        HashSet<Integer> a = Sets.newHashSet(1, 2, 3, 4, 5);
        HashSet<Integer> b = Sets.newHashSet(4, 5, 6, 7, 8);
        Sets.SetView<Integer> union = Sets.union(a, b);
        ImmutableList<@NonNull Integer> unionList = union.immutableCopy().asList();
        System.out.println(unionList);
        //差集 a-b
        Sets.SetView<Integer> diff = Sets.difference(a, b);
        ImmutableList<@NonNull Integer> diffList = diff.immutableCopy().asList();
        System.out.println(diffList);
        //差集 b-a
        System.out.println(Sets.difference(b, a).immutableCopy().asList());
        //对称差集
        ImmutableList<@NonNull Integer> symmetricDiffList = Sets.symmetricDifference(a, b).immutableCopy().asList();
        System.out.println(symmetricDiffList);
        //交集
        ImmutableList<@NonNull Integer> intersectionList = Sets.intersection(a, b).immutableCopy().asList();
        System.out.println(intersectionList);



    }

    @Test
    public void JoinerTest(){
        StringBuilder stringBuilder = new StringBuilder("hello");
        // 字符串连接器，以|为分隔符，同时去掉null元素
        Joiner joiner1 = Joiner.on("|").skipNulls();
        // 构成一个字符串foo|bar|baz并添加到stringBuilder
        //stringBuilder = joiner1.appendTo(stringBuilder, "foo", "bar", null, "baz");
        //System.out.println(stringBuilder); // hellofoo|bar|baz
        String res = joiner1.join("foo", "bar", null, "baz");
        System.out.println(res);
    }

    @Test
    public void SplitterTest(){
        // 分割符为|，并去掉得到元素的前后空白
        Splitter sp = Splitter.on("|").trimResults();
        String str = "hello | world | your | Name ";
        Iterable<String> iterable = sp.split(str);
        System.out.println(iterable);

        String letterAndNumber = "1234abcdABCD56789";
        // 保留数字
        String number = CharMatcher.digit().retainFrom(letterAndNumber);
        System.out.println(number);// 123456789

        String str2 = "1-2-3-4-  5-  6   ";
        List<String> list = Splitter.on("-").omitEmptyStrings().trimResults().splitToList(str2);
        System.out.println(list);
        //转Map
        Map<String, String> map = Splitter.on(",").withKeyValueSeparator("=").split("xiaoming=11,xiaohong=23");
        System.out.println(map);
    }
}
