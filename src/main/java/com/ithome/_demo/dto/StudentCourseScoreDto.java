package com.ithome._demo.dto;

import com.ithome._demo.common.utils.DateUtil;
import com.ithome._demo.model.report.StudentCourseScoreReportModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseScoreDto {
    private Integer studentId;

    private String firstName;

    private String lastName;

    private String gender;

    private String grade;

    private String departmentId;

    private String departmentName;

    private String departmentDesc;

    private Integer courseId;

    private String courseName;

    private String courseDesc;

    private String credit;

    private Integer scoreId;

    private Integer score;

    private Date testDate;

    public StudentCourseScoreReportModel toStudentCourseScoreReportModel() {
        StudentCourseScoreReportModel model = new StudentCourseScoreReportModel();
        String padding = StringUtils.leftPad(String.valueOf(studentId), 5, "0");
        model.setStudentNumber(padding);
        model.setFullName(firstName + " " + lastName);
        model.setGrade(grade);
        model.setDepartmentDesc(departmentDesc);
        model.setCourseDesc(courseDesc);
        model.setScore(score);
        model.setTestDate(DateUtil.formatDate(DateUtil.toLocalDate(testDate), DateUtil.DatePattern.DATE_SLASH));
        return model;
    }
}
