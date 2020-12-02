package com.transformers.service;

import com.transformers.entity.User;
import com.transformers.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User get(Long id) {
        return userMapper.get(id);
    }

    public List<User> list() {
        return userMapper.list();
    }

    public List<User> listWithout() {
        return userMapper.list();
    }

    public int insert(User user) {
        return userMapper.insert(user);
    }
}
