package com.transformers.dao;

import com.transformers.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface UserDao {
    User get(@Param("id") long id);
    List<User> list();
}
