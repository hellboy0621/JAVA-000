package com.transformers.homework.bean.constructor;

import com.transformers.homework.bean.setter.AnotherBean;
import com.transformers.homework.bean.setter.YetAnotherBean;

public class ExampleBean {
    private AnotherBean beanOne;

    private YetAnotherBean beanTwo;

    private int i;

    public ExampleBean(
            AnotherBean anotherBean, YetAnotherBean yetAnotherBean, int i) {
        this.beanOne = anotherBean;
        this.beanTwo = yetAnotherBean;
        this.i = i;
    }
}
