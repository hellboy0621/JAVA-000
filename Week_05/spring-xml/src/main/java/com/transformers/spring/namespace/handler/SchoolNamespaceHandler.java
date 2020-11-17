package com.transformers.spring.namespace.handler;

import com.transformers.spring.namespace.parser.KlassBeanDefinitionParser;
import com.transformers.spring.namespace.parser.SchoolBeanDefinitionParser;
import com.transformers.spring.namespace.tag.KlassBeanDefinitionTag;
import com.transformers.spring.namespace.tag.SchoolBeanDefinitionTag;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Spring namespace handler for school.
 */
public class SchoolNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser(SchoolBeanDefinitionTag.ROOT_TAG, new SchoolBeanDefinitionParser());
        registerBeanDefinitionParser(KlassBeanDefinitionTag.ROOT_TAG, new KlassBeanDefinitionParser());
    }
}
