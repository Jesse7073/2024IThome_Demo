package com.ithome._demo.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "course", schema = "ithome2024")
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Course_Id")
    private Integer courseId;

    @Column(name = "Course_Name")
    private String courseName;

    @Column(name = "Course_Description")
    private String courseDesc;

    @Column(name = "Credit")
    private String credit;
}
