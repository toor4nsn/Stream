package com.example.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Liwei
 * @Date 2021/7/3 15:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("student")
public class Student {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer age;

    public Student(Integer id) {
        this.id = id;
    }
}


