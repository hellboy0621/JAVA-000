package com.transformers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Student implements Serializable {
    
    private int id;
    private String name;

    public void init() {
        System.out.println("com.transformers.entity.Student.init hello...");
    }

    public Student create() {
        return new Student(101, "hellboy0621");
    }

}
