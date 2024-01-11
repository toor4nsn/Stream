package com.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UsersService extends IService<Users>{
    List<Users> queryListByName(String name);
    Page<Users> queryList(Users users, Integer pageNum, Integer pageSize);

}
