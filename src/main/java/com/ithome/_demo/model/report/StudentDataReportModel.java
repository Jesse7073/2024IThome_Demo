package com.ithome._demo.model.report;

import com.ithome._demo.common.consts.GenderConsts;
import com.ithome._demo.dto.StudentAndDepartmentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDataReportModel {
    private String studentNumber;

    private String fullName;

    private String gender;

    private String grade;

    private String departmentDesc;

    public StudentDataReportModel(StudentAndDepartmentDto dto) {
        this.studentNumber = StringUtils.leftPad(String.valueOf(dto.getStudentId()), 5, "0");
        this.fullName = dto.getFirstName() + " " + dto.getLastName();
        this.gender = GenderConsts.Gender.MALE.getCode().equals(dto.getGender()) ? GenderConsts.Gender.MALE.getValue() : GenderConsts.Gender.FEMALE.getValue();
        this.grade = dto.getGrade();
        this.departmentDesc = dto.getDepartmentDesc();
    }
}
