package com.ithome._demo.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "student", schema = "ithome2024")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Student_Id")
    private Integer studentId;

    @Column(name = "First_Name")
    private String firstName;

    @Column(name = "Last_Name")
    private String lastName;

    @Column(name = "Gender")
    private String gender;

    @Column(name = "Grade")
    private String grade;

    @Column(name = "Department_Id")
    private String departmentId;
}
