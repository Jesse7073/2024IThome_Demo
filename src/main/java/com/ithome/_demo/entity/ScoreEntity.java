package com.ithome._demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "score", schema = "ithome2024")
public class ScoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Score_Id")
    private Integer scoreId;

    @Column(name = "Student_Id")
    private Integer studentId;

    @Column(name = "Course_Id")
    private Integer courseId;

    @Column(name = "Score")
    private String score;

    @Column(name = "Test_Date")
    private Date testDate;
}
