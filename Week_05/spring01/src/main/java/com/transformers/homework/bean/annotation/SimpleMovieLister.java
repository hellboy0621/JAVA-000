package com.transformers.homework.bean.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 1.属性上使用@Autowired
 * 2.setter方法上使用@Autowired
 * 3.属性上使用@Resource
 * 4.setter方法上使用@Resource
 */
@Component
public class SimpleMovieLister {

    @Autowired
//    @Resource
    private MovieFinder movieFinder;

//    @Autowired
//    @Resource
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }
}
