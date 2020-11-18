package com.transformers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class School {

    private Klass klass;

    private Student student;

    public void ding() {
        System.out.println("School.ding");
        System.out.println("Class1 have " + this.klass.getStudents().size() + " students and one is " + this.student);
    }
}
