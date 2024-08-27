package com.ithome._demo.model.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDataReportModel {
    private Integer studentId;

    private String fullName;

    private String gender;

    private String grade;

    private String department;
}
