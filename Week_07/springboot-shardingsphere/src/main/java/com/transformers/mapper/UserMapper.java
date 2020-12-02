package com.transformers.mapper;

import com.transformers.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User get(@Param("id") long id);
    List<User> list();
    int insert(@Param("user") User user);
}
