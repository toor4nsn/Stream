package com.example.po;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class Item {
    private String name;
    private int qty;
    private BigDecimal price;
}