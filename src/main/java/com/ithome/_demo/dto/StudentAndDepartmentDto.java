package com.ithome._demo.dto;

import com.ithome._demo.common.consts.GenderConsts;
import com.ithome._demo.model.report.StudentDataReportModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAndDepartmentDto {
    private Integer studentId;

    private String firstName;

    private String lastName;

    private String gender;

    private String grade;

    private Integer departmentId;

    private String departmentName;

    private String departmentDesc;

    public StudentDataReportModel toStudentDataReportModel() {
        StudentDataReportModel model = new StudentDataReportModel();
        String padding = StringUtils.leftPad(String.valueOf(studentId), 5, "0");
        model.setStudentNumber(padding);
        model.setFullName(firstName + " " + lastName);
        model.setGender(GenderConsts.Gender.MALE.getCode().equals(gender) ? GenderConsts.Gender.MALE.getValue() : GenderConsts.Gender.FEMALE.getValue());
        model.setGrade(grade);
        model.setDepartmentDesc(departmentDesc);
        return model;
    }
}
