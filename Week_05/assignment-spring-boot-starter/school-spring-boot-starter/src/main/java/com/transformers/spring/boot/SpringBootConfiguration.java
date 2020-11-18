package com.transformers.spring.boot;

import com.transformers.entity.Klass;
import com.transformers.entity.School;
import com.transformers.entity.Student;
import com.transformers.spring.boot.prop.SpringBootPropertiesConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(SpringBootPropertiesConfiguration.class)
@ConditionalOnProperty(prefix = "spring.school", name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class SpringBootConfiguration {

    private final SpringBootPropertiesConfiguration props;

    @Bean("studentList")
    public List<Student> studentList() {
        List<Student> studentList = new ArrayList<Student>();
        Iterator<Map.Entry<Object, Object>> iterator = this.props.getProps().entrySet().iterator();
        Student student = null;
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> next = iterator.next();
            if (String.valueOf(next.getKey()).contains("students")) {
                String[] stu = String.valueOf(next.getValue()).split("@_@");
                student = new Student(Integer.valueOf(stu[0]), stu[1]);
                studentList.add(student);
            }
        }
        return studentList;
    }

    @Bean
    @DependsOn({"studentList"})
    public Klass klass(@Qualifier("studentList") List<Student> studentList) {
        Klass klass = new Klass();
        klass.setStudents(studentList);
        return klass;
    }

    @Bean
    @DependsOn({"klass", "studentList"})
    public School school(Klass klass, @Qualifier("studentList") List<Student> studentList) {
        School school = new School();
        school.setKlass(klass);
        school.setStudent(studentList.get(0));
        return school;
    }

}
