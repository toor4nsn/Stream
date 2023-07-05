package com.date;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @Author Liwei
 * @Date 2022/9/29 20:40
 */
public class LocalDateTimeTest {
    @Test
    public void mytest() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        System.out.println(date);
        System.out.println(time);

        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        System.out.println(now.getYear());
        System.out.println(now.getMonth());
        System.out.println(now.getDayOfMonth());

        LocalDate cusDate = LocalDate.of(2022, Month.SEPTEMBER, 29);
        LocalTime cusTime = LocalTime.of(20, 46, 31);
        LocalDateTime cusDateTime = LocalDateTime.of(cusDate, cusTime);
        System.out.println(cusDateTime);


        // 计算昨天的同一时刻（由于对象不可修改，所以返回的是新对象）
        LocalDateTime yesterday = now.plus(-1, ChronoUnit.DAYS);
        System.out.println(yesterday);


        // 计算当天的00点和 24点
        LocalDateTime begin = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        System.out.println(begin);
        System.out.println(end);

        System.out.println("计算一周、一个月、一年前的当前时刻：");
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime oneWeekAgo = today.minus(1, ChronoUnit.WEEKS);
        LocalDateTime oneMonthAgo = today.minus(1, ChronoUnit.MONTHS);
        LocalDateTime oneYearAgo = today.minus(1, ChronoUnit.YEARS);
        System.out.println(oneWeekAgo);
        System.out.println(oneMonthAgo);
        System.out.println(oneYearAgo);
        System.out.println(today.plusHours(1));

        // 将day修改为6号
        LocalDateTime modifiedDateTime = today.with(ChronoField.DAY_OF_MONTH, 6);
        System.out.println(modifiedDateTime);
        System.out.println(today.withDayOfMonth(6));

    }

    @Test
    public void compare() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after = now.plusHours(1);
        boolean isAfter = after.isAfter(now);
        System.out.println(isAfter);

        // 时区（id的形式），默认的是本国时区
        ZoneId zoneId = ZoneId.systemDefault();
        // 为localDateTime补充时区信息
        ZonedDateTime beijingTime = now.atZone(zoneId);
        System.out.println("beijingTime:" + beijingTime);
    }

    /**
     * Date => LocalDateTime
     */
    @Test
    public void DateToLocalDateTime(){
        Date original = new Date();

        LocalDateTime localDateTime = original.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println(original);
        System.out.println(localDateTime);


        LocalDateTime localDateTime2 = LocalDateTime.ofInstant(original.toInstant(), ZoneId.systemDefault());
        System.out.println(localDateTime2);

    }

    /**
     *   LocalDateTime => Date
     */
    @Test
    public void LocalDateTimeToDate(){
        LocalDateTime now = LocalDateTime.now();
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println(date);
    }

    /**
     * LocalDateTime <=> Second
     */
    @Test
    public void LocalDateTimeToSecond(){
        Date date = new Date();
        System.out.println(date.getTime() / 1000);


        //LocalDateTime -> second
        LocalDateTime now = LocalDateTime.now();
        long l = now.toEpochSecond(ZoneOffset.ofHours(8));
        System.out.println(l);


        //second   -> LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(l, 0, ZoneOffset.ofHours(8));
        System.out.println(localDateTime);

    }

    /**
     * 格式化
     */
    @Test
    public void format() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("格式化前:" + now);
        String format = now.format(DateTimeFormatter.ISO_DATE_TIME);
        System.out.println("默认格式:" + format);
        String other = now.format(DateTimeFormatter.BASIC_ISO_DATE);
        System.out.println("其他格式:" + other);
        //格式化
        String formatted = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        System.out.println(formatted);

        //解析
        LocalDateTime parse = LocalDateTime.parse(formatted, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        System.out.println(parse);

        String formatted2 = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(formatted2);


        String s = DateUtil.formatLocalDateTime(now);
        System.out.println(s);

        // 解析自定义格式时间
        LocalDateTime localDateTime = LocalDateTimeUtil.parse("2022-09-30 01:25:07", DatePattern.NORM_DATETIME_PATTERN);
        System.out.println(localDateTime);

    }

    /**
     * 当前时间-到当天结束时间的差值 秒
     */
    @Test
    public void between() {
        Date now = new Date();
        long offset = DateUtil.between(now, DateUtil.endOfDay(now), DateUnit.SECOND);
        System.out.println(offset);
    }
}
