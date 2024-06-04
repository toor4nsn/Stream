package com.example.mapper;

import com.example.entity.TUser;

public interface TUserMapper {
    int insert(TUser record);

    TUser selectByPrimaryKey(Long id);
}