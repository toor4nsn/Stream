package com.example.po;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AccountInfo {
    @ExcelProperty("账户ID")
    private Integer id;
    @ExcelProperty("账户名称")
    private String name;
    @ExcelProperty("公司名称")
    private String companyName;
    @ExcelProperty("网站名称")
    private String siteName;
    @ExcelProperty("网站url")
    private String siteUrl;
}
