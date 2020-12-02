package com.transformers.service;

import com.transformers.config.DataSource;
import com.transformers.dao.UserDao;
import com.transformers.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @DataSource
    public User get(Long id) {
        return userDao.get(id);
    }

    @DataSource("slave")
    public List<User> list() {
        return userDao.list();
    }

    public List<User> listWithout() {
        return userDao.list();
    }
}
