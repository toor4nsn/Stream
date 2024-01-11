package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * movie rating
 * </p>
 *
 * @author zephyr
 * @since 2024-01-02
 */
@Getter
@Setter
@TableName("cinema")
public class Cinema {
    @TableField("id")
    private Integer id;

    private String movie;

    private String description;

    private Double rating;
}
