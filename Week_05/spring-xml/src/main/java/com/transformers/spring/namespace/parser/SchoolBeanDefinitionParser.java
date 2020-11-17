package com.transformers.spring.namespace.parser;

import com.transformers.entity.School;
import com.transformers.spring.namespace.tag.SchoolBeanDefinitionTag;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class SchoolBeanDefinitionParser extends AbstractBeanDefinitionParser {

    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(School.class);
        factory.addConstructorArgValue(parseKlass(element));
        factory.addConstructorArgValue(parseStudent(element));
        return factory.getBeanDefinition();
    }

    private RuntimeBeanReference parseKlass(Element element) {
        String klass = element.getAttribute(SchoolBeanDefinitionTag.KLASS_REFS_TAG);
        return new RuntimeBeanReference(klass);
    }

    private RuntimeBeanReference parseStudent(Element element) {
        String student = element.getAttribute(SchoolBeanDefinitionTag.STUDENT_REFS_TAG);
        return new RuntimeBeanReference(student);
    }

}
