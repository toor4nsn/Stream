package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "users")
public class Users {
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @TableField(value = "`name`")
    private String name;

    @TableField(value = "mail")
    private String mail;
}