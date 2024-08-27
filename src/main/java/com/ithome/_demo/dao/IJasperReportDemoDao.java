package com.ithome._demo.dao;

import com.ithome._demo.dto.StudentAndDepartmentDto;

import java.util.List;

public interface IJasperReportDemoDao {
    // 查詢學生資料
    List<StudentAndDepartmentDto> queryStudentData();
}
