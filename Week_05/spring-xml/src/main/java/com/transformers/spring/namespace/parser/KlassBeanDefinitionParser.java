package com.transformers.spring.namespace.parser;

import com.google.common.base.Splitter;
import com.transformers.entity.Klass;
import com.transformers.spring.namespace.tag.KlassBeanDefinitionTag;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.List;

public class KlassBeanDefinitionParser extends AbstractBeanDefinitionParser {

    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(Klass.class);
        factory.addConstructorArgValue(parseStudents(element));
        return factory.getBeanDefinition();
    }

    private Collection<RuntimeBeanReference> parseStudents(Element element) {
        List<String> studentList = Splitter.on(",").trimResults().splitToList(element.getAttribute(KlassBeanDefinitionTag.STUDENTS_REFS_TAG));
        Collection<RuntimeBeanReference> result = new ManagedList<RuntimeBeanReference>(studentList.size());
        for (String each : studentList) {
            result.add(new RuntimeBeanReference(each));
        }
        return result;
    }

}
