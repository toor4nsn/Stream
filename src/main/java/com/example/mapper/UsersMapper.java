package com.example.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Users;

public interface UsersMapper extends BaseMapper<Users> {
    default List<Users> selectAllByNameLambda(String name) {
        LambdaQueryWrapper<Users> myQuery = Wrappers.lambdaQuery(Users.class);
        myQuery.eq(Users::getName, name);
        return selectList(myQuery);
    }

    List<Users> selectAllByName(@Param("name") String name);


    List<Users> selectByUserId(@Param("ids") List<Integer> ids);


}