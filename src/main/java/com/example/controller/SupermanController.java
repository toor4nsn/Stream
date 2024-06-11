package com.example.controller;

import com.example.entity.Superman;
import com.example.service.SupermanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SupermanController {

    @Autowired
    private SupermanService supermanService;

    /**
     *  全量嵌套查询，数据具备树形结构
     * @return
     */
    @GetMapping("/getAllSuperman")
    public List getAllSuperman() {
        // supermanService才是重点，接下来演示三种全量嵌套查询的方法
        List<Superman> list = supermanService.getAllSupermanV3();
        return list;
    }
    /**
     * 根据pid分步查询（实际开发最常用）
     * @param pid
     * @return
     */
    @GetMapping("getSupermanByPid")
    public List getSupermanByPid(@RequestParam Integer pid) {
        List<Superman> list = supermanService.getSupermanByPid(pid);
        return list;
    }
}