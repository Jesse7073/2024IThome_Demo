package com.ithome._demo.dto;

import com.ithome._demo.entity.StudentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private Integer studentId;

    private String firstName;

    private String LastName;

    private String gender;

    private String grade;

    private String department;

    public StudentDto(StudentEntity studentEntity) {
        this.studentId = studentEntity.getStudentId();
        this.firstName = studentEntity.getFirstName();
        this.LastName = studentEntity.getLastName();
        this.gender = studentEntity.getGender();
        this.grade = studentEntity.getGrade();
        this.department = studentEntity.getDepartment();
    }
}
