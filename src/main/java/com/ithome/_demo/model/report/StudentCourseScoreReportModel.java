package com.ithome._demo.model.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourseScoreReportModel {
    private String studentNumber;

    private String fullName;

    private String grade;

    private String department;

    private String courseDesc;

    private String departmentDesc;

    private Integer score;

    private String testDate;
}
