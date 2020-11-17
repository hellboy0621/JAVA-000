package com.transformers.homework.aop.service.impl;

import com.transformers.homework.aop.service.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public void sayHello(String name) {
        if (name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("parameter can not be null or empty.");
        }
        System.out.println("hello " + name);
    }
}
