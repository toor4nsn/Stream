package com.example.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "student")
public class Student {
    /**
     * PK
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 名字
     */
    @NotNull(message = "名字不能为空")
    @TableField(value = "`name`")
    private String name;

    /**
     * 年龄
     */
    @NotNull(message = "年龄不能为空")
    @Max(value = 35, message = "年龄不超过35")
    @Min(value = 18, message = "年龄不小于18")
    @TableField(value = "age")
    private Integer age;


}