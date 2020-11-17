package com.transformers.homework.bean.annotation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                "classpath:applicationContext-bean-annotation.xml");
        String[] beanDefinitionNames = ctx.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
        SimpleMovieLister bean = ctx.getBean(SimpleMovieLister.class);
        System.out.println();
    }
}
