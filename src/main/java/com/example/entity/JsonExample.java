package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "json_example")
public class JsonExample {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "`data`")
    private String data;
}