package com.transformers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Klass {

    private List<Student> students;

    public void dong() {
        System.out.println("com.transformers.entity.Klass.dong");
        System.out.println(this.getStudents());
    }
}
