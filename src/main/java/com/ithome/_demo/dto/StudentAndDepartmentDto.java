package com.ithome._demo.dto;

import com.ithome._demo.common.consts.DepartmentConsts;
import com.ithome._demo.common.consts.GenderConsts;
import com.ithome._demo.entity.StudentEntity;
import com.ithome._demo.model.report.StudentDataReportModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private Integer studentId;

    private String firstName;

    private String LastName;

    private String gender;

    private String grade;

    private String departmentId;

    private String departmentName;

    public StudentDto(StudentEntity studentEntity) {
        this.studentId = studentEntity.getStudentId();
        this.firstName = studentEntity.getFirstName();
        this.LastName = studentEntity.getLastName();
        this.gender = studentEntity.getGender();
        this.grade = studentEntity.getGrade();
        this.department = studentEntity.getDepartment();
    }

    public StudentDataReportModel toStudentDataReportModel() {
        StudentDataReportModel model = new StudentDataReportModel();
        model.setStudentId(studentId);
        model.setFullName(firstName + " " + LastName);
        model.setGender(GenderConsts.Gender.MALE.getCode().equals(gender) ? GenderConsts.Gender.MALE.getValue() : GenderConsts.Gender.FEMALE.getValue());
        model.setGrade(grade);

        switch (department) {
            case "Pharmacy":
                department = DepartmentConsts.Department.PHARMACY.getValue();
                break;
            case "Finance":
                department = DepartmentConsts.Department.FINANCE.getValue();
                break;
            case "Accounting":
                department = DepartmentConsts.Department.ACCOUNTING.getValue();
                break;
            case "Computer Science":
                department = DepartmentConsts.Department.COMPUTER_SIENCE.getValue();
                break;
        }

        model.setDepartment(department);
        return model;
    }
}
