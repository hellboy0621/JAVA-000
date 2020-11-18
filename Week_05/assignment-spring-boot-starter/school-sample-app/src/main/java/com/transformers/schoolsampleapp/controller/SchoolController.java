package com.transformers.schoolsampleapp.controller;

import com.transformers.entity.Klass;
import com.transformers.entity.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/school")
public class SchoolController {

    @Autowired
    private School school;

    @Autowired
    private Klass klass;

    @GetMapping("/hello")
    public String hello() {
        school.ding();
        klass.dong();
        return "hello";
    }

}
