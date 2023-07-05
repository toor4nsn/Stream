package com.example.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author Liwei
 * @Date 2022/4/15 21:10
 */
@Data
public class Product {
    private String id;
    private String mediaSource;
    private List<ProductInfo> infoList;
    private BigDecimal price;
}
