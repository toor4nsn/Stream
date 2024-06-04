package com.example.entity;

import java.util.Date;

import com.example.enums.WeekDayEnum;
import lombok.Data;

@Data
public class TUser {
    /**
    * 主键id
    */
    private Long id;

    /**
    * 姓名
    */
    private String name;

    /**
    * 年龄
    */
    private Integer age;

    /**
     * 休息日，实际数据库字段是tinyint或varchar
     */
    private WeekDayEnum restDay;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 修改时间
    */
    private Date updateTime;

    /**
    * 是否删除
    */
    private Boolean deleted;
}