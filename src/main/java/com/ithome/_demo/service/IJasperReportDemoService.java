package com.ithome._demo.service;

import com.ithome._demo.dto.StudentAndDepartmentDto;

import java.util.List;

public interface IJasperReportDemoService {
    // 查詢學生資料
    List<StudentAndDepartmentDto> getStudentAndDepartmentData();
}
