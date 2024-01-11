package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Users;
import com.example.mapper.UsersMapper;
import com.example.service.UsersService;

import javax.annotation.Resource;

@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService{
    @Resource
    UsersMapper usersMapper;
    @Override
    public List<Users> queryListByName(String name) {
        return usersMapper.selectAllByName(name);
    }

    @Override
    public Page<Users> queryList(Users users,Integer pageNum,Integer pageSize) {
        Page<Users> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page.setMaxLimit(100L);
        LambdaQueryWrapper<Users> wrapper = Wrappers.lambdaQuery(Users.class);
        Users users2 = Optional.ofNullable(users).orElse(new Users());
        boolean var1 = Optional.ofNullable(users).map(Users::getUserId).isPresent();
        boolean var2 = Optional.ofNullable(users).map(Users::getName).isPresent();
        boolean var3 = Optional.ofNullable(users).map(Users::getUserId).isPresent();

        Optional.ofNullable(users).map(Users::getUserId).ifPresent(it->{
            wrapper.eq(Users::getUserId,it);
        });
        Optional.ofNullable(users).map(Users::getName).ifPresent(it->{
            wrapper.eq(Users::getName,it);
        });
        Optional.ofNullable(users).map(Users::getMail).ifPresent(it->{
            wrapper.eq(Users::getMail,it);
        });

        Page<Users> selectPage = usersMapper.selectPage(page, wrapper);
        return selectPage;
    }
}
