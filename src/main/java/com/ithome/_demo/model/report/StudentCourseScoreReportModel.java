package com.ithome._demo.model.report;

import com.ithome._demo.common.utils.DateUtil;
import com.ithome._demo.dto.StudentCourseScoreDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

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

    public StudentCourseScoreReportModel(StudentCourseScoreDto dto) {
        this.studentNumber = StringUtils.leftPad(String.valueOf(dto.getStudentId()), 5, "0");
        this.fullName = dto.getFirstName() + " " + dto.getLastName();
        this.grade = dto.getGrade();
        this.departmentDesc = dto.getDepartmentDesc();
        this.courseDesc = dto.getCourseDesc();
        this.score = dto.getScore();
        this.testDate = DateUtil.formatDate(DateUtil.toLocalDate(dto.getTestDate()), DateUtil.DatePattern.DATE_SLASH);
    }
}
