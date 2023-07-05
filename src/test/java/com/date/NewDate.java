package com.date;


import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author Liwei
 * @Date 2021/7/31 23:49
 */

public class NewDate {
    @Test
    public void test01() {
        //Date oldDate = new Date();
        //System.out.println(oldDate);

        LocalDate newDate = LocalDate.now();
        System.out.println(newDate);

        LocalTime newTime = LocalTime.now();
        System.out.println(newTime);

        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        System.out.println("=============");
        LocalDate customDate = LocalDate.of(2020, Month.DECEMBER, 5);
        LocalTime customTime = LocalTime.of(16, 30, 0);
        System.out.println(customDate);
        System.out.println(customTime);
        LocalDateTime cutomDateTime = LocalDateTime.of(customDate, customTime);
        System.out.println(cutomDateTime);
        System.out.println("=============");
        System.out.println("获取时间参数的年、月、日：");
        LocalDateTime param = LocalDateTime.now();

        int year = param.getYear();
        System.out.println(year);
        Month month = param.getMonth();
        System.out.println(month);
        int dayOfMonth = param.getDayOfMonth();
        System.out.println(dayOfMonth);
        int hour = param.getHour();
        System.out.println(hour);
        int minute = param.getMinute();
        System.out.println(minute);
        int second = param.getSecond();
        System.out.println(second);

        System.out.println("我是分隔符");
        System.out.println(param);
        LocalDateTime yesterday = param.plus(-1, ChronoUnit.DAYS);
        System.out.println(yesterday);

        LocalDateTime beforeOneHour = LocalDateTime.now().plus(-1, ChronoUnit.HOURS);
        System.out.println(beforeOneHour);

        // 计算当天的00点和24点（你看，这里就看到组合的威力了）
        System.out.println("计算当天的00点和24点：");
        LocalDateTime todayBegin = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        System.out.println(todayBegin);
        System.out.println(todayEnd);
        System.out.println("计算一周、一个月、一年前的当前时刻：");
        param = LocalDateTime.now();
        System.out.println(param.minus(1, ChronoUnit.WEEKS));
        System.out.println(param.minus(1, ChronoUnit.MONTHS));
        System.out.println(param.minus(1, ChronoUnit.YEARS));
        System.out.println("我是分隔符");
        System.out.println(param.plusWeeks(1));
        System.out.println(param.plusMonths(1));
        System.out.println(param.plusYears(1));

        System.out.println("我是分隔符");
        LocalDateTime dayModify = param.with(ChronoField.DAY_OF_MONTH, 9);
        LocalDateTime monthModify = param.with(ChronoField.MONTH_OF_YEAR, 10);
        System.out.println(dayModify);
        System.out.println(monthModify);
        System.out.println("=============");
        System.out.println(param.withDayOfMonth(9));
        System.out.println(param.withMonth(10));
        System.out.println("我是分隔符");
        LocalDateTime plusFiveHour = param.plusHours(5);
        System.out.println(plusFiveHour.isAfter(param));
        System.out.println(plusFiveHour.isBefore(param));
        System.out.println(plusFiveHour.isEqual(param));

        ZoneId zoneId = ZoneId.systemDefault();
        System.out.println(zoneId);
        ZonedDateTime zonedDateTime = param.atZone(zoneId);
        System.out.println(zonedDateTime);

        System.out.println("=============");

    }

    @Test
    public void test02() {
        //LocalDateTime与Date互转      //媒介是Instant（格林尼治时间）
        //LocalDateTime -> Date
        LocalDateTime nowDateTime = LocalDateTime.now();
        ZonedDateTime zoneNowDateTime = nowDateTime.atZone(ZoneId.systemDefault());
        System.out.println(zoneNowDateTime);
        Instant instant = zoneNowDateTime.toInstant();
        System.out.println(instant);
        Date dateFromLocalDateTime = Date.from(instant);
        System.out.println(dateFromLocalDateTime);
        System.out.println("我是分隔符");
        //简洁版 LocalDateTime to Date
        Date date2 = Date.from(nowDateTime.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println(date2);

        //Date -> LocalDateTime
        Date oldDate = new Date();
        Instant dateToInstant = oldDate.toInstant();
        System.out.println(dateToInstant);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(dateToInstant, ZoneId.systemDefault());
        System.out.println("localDateTime:" + localDateTime);
        //简洁版 Date to LocalDateTime
        LocalDateTime localDateTime2 = oldDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println("localDateTime2:" + localDateTime2);


        System.out.println("=============");
        //转成秒
        Date oldDate2 = new Date();
        System.out.println(oldDate2);
        long millisecondsTime = oldDate2.getTime();
        long secondTime = millisecondsTime / 1000;
        System.out.println(secondTime);
        long secondLocalDateTime = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8));
        System.out.println(secondLocalDateTime);
        //秒解析成LocalDateTime
        System.out.println(LocalDateTime.ofEpochSecond(secondLocalDateTime, 0, ZoneOffset.ofHours(8)));
    }

    @Test
    public void test03() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("格式化前:" + now);
        String format = now.format(DateTimeFormatter.ISO_DATE_TIME);
        System.out.println("默认格式:" + format);
        String other = now.format(DateTimeFormatter.BASIC_ISO_DATE);
        System.out.println("其他格式:" + other);
        String format2 = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        System.out.println("格式化后:" + format2);
        //反格式化
        LocalDateTime parse = LocalDateTime.parse(format2, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        System.out.println(parse);
    }

    @Test
    public void test04() {
        ArrayList<Integer> a = Lists.newArrayList(1, 2, 3);
        ArrayList<Integer> b = Lists.newArrayList(4, 5, 6);
        a.addAll(b);
        System.out.println(a);
    }


    @Autowired
    private Validator validator;

    //DTO对象校验
    private <T> void checkParam(T t) {
        Set<ConstraintViolation<T>> violationSet = validator.validate(t);
        if (CollectionUtils.isNotEmpty(violationSet)) {
            String msg = violationSet.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(";"));
            throw new IllegalArgumentException(msg);
        }
    }

    //List校验
    private <T> void listCheck(List<T> list) {
        Set<String> msgList = list.stream()
                .map(it -> validator.validate(it))
                .flatMap(err -> err.stream().map(ConstraintViolation::getMessage))
                .collect(Collectors.toSet());

        if (CollectionUtils.isNotEmpty(msgList)) {
            throw new IllegalArgumentException(String.join(";", msgList));
        }
    }
}
