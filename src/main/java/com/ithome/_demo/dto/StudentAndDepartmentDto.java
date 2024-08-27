package com.ithome._demo.dto;

import com.ithome._demo.common.consts.GenderConsts;
import com.ithome._demo.entity.StudentEntity;
import com.ithome._demo.model.report.StudentDataReportModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAndDepartmentDto {
    private Integer studentId;

    private String firstName;

    private String lastName;

    private String gender;

    private String grade;

    private String departmentId;

    private String departmentName;

    private String departmentDesc;

    public StudentDataReportModel toStudentDataReportModel() {
        StudentDataReportModel model = new StudentDataReportModel();
        model.setStudentId(studentId);
        model.setFullName(firstName + " " + lastName);
        model.setGender(GenderConsts.Gender.MALE.getCode().equals(gender) ? GenderConsts.Gender.MALE.getValue() : GenderConsts.Gender.FEMALE.getValue());
        model.setGrade(grade);
        model.setDepartment(departmentDesc);
        return model;
    }
}
