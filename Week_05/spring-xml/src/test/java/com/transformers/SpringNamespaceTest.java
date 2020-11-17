package com.transformers;

import com.transformers.entity.Klass;
import com.transformers.entity.School;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@ContextConfiguration(locations = "classpath:META-INF/spring/application-context.xml")
public class SpringNamespaceTest extends AbstractJUnit4SpringContextTests {

    @Resource
    private School school;

    @Resource
    private Klass klass;

    @Test
    public void assertShardingTransactionTypeScanner() {
        assertThat(school.getKlass().getStudents().size(), is(3));
        assertThat(school.getStudent().getId(), is(103));
        assertThat(school.getStudent().getName(), is("stu103"));
        assertThat(klass.getStudents().size(), is(3));
        System.out.println(school);
        System.out.println(klass);
    }

    /**
     * School(klass=Klass(students=[Student(id=101, name=stu101), Student(id=102, name=stu102), Student(id=103, name=stu103)]), student=Student(id=103, name=stu103))
     * Klass(students=[Student(id=101, name=stu101), Student(id=102, name=stu102), Student(id=103, name=stu103)])
     *
     * Process finished with exit code 0
     */

}
