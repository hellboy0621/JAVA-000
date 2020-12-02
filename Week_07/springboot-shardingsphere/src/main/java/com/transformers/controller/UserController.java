package com.transformers.controller;

import com.transformers.entity.User;
import com.transformers.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User get(@PathVariable("id") Long id) {
        return userService.get(id);
    }

    @GetMapping("/list")
    public List<User> list() {
        return userService.list();
    }

    @GetMapping("/listWithout")
    public List<User> listWithout() {
        return userService.listWithout();
    }

    @PostMapping("/insert")
    public User insert(@RequestBody User user) {
        userService.insert(user);
        return user;
    }
}
