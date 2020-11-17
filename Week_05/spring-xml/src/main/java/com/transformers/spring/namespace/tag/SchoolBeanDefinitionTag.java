package com.transformers.spring.namespace.tag;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * school bean definition tag.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SchoolBeanDefinitionTag {

    public static final String ROOT_TAG = "school";

    public static final String KLASS_REFS_TAG = "klass-refs";

    public static final String STUDENT_REFS_TAG = "student-refs";
}
