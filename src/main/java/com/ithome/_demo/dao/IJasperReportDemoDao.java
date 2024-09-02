package com.ithome._demo.dao;

import com.ithome._demo.dto.StudentAndDepartmentDto;
import com.ithome._demo.dto.StudentCourseScoreDto;

import java.util.List;

public interface IJasperReportDemoDao {
    // 查詢學生資料
    List<StudentAndDepartmentDto> getStudentAndDepartmentData();

    // 查詢學生考試成績資料
    List<StudentCourseScoreDto> getStudentCourseScoreData();
}
