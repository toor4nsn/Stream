package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

@Data
@TableName(value = "t_superman")
public class Superman {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "`name`")
    private String name;

    @TableField(value = "`open`")
    private Byte open;

    @TableField(value = "pid")
    private Integer pid;

    // 前端JS对象的children数组(对应List)里存的还是JS对象（对应Superman），所以是List<Superman>
    //@TableField(exist = false)
    private List<Superman> children = new ArrayList<>();
}