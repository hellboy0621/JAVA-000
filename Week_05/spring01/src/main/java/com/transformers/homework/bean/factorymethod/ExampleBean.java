package com.transformers.homework.bean.factorymethod;

import com.transformers.homework.bean.setter.AnotherBean;
import com.transformers.homework.bean.setter.YetAnotherBean;

public class ExampleBean {
    private AnotherBean beanOne;

    private YetAnotherBean beanTwo;

    private int i;

    private ExampleBean(AnotherBean beanOne, YetAnotherBean beanTwo, int i) {
        this.beanOne = beanOne;
        this.beanTwo = beanTwo;
        this.i = i;
    }

    // a static factory method; the arguments to this method can be
    // considered the dependencies of the bean that is returned,
    // regardless of how those arguments are actually used.
    public static ExampleBean createInstance(
            AnotherBean anotherBean, YetAnotherBean yetAnotherBean, int i) {
        ExampleBean eb = new ExampleBean(anotherBean, yetAnotherBean, i);
        // some other operations...
        return eb;
    }
}
