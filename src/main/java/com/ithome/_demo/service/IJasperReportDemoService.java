package com.ithome._demo.service;

import com.ithome._demo.dto.StudentAndDepartmentDto;
import com.ithome._demo.dto.StudentCourseScoreDto;

import java.util.List;

public interface IJasperReportDemoService {
    // 查詢學生與科系資料
    List<StudentAndDepartmentDto> getStudentAndDepartmentData();

    // 查詢學生考試成績資料
    List<StudentCourseScoreDto> getStudentCourseScoreData();

    // 查詢科系平均成績
//    List getDepartmentAverageScore();
}
