package com.ithome._demo.service;

import com.ithome._demo.dto.DepartmentAverageScoreDto;
import com.ithome._demo.model.report.DepartmentCourseScoreAverageReportModel;
import com.ithome._demo.model.report.StudentCourseScoreReportModel;
import com.ithome._demo.model.report.StudentDataReportModel;
import com.ithome._demo.vo.DatePeriodVo;

import java.util.List;

public interface IReportDemoService {
    // 查詢學生與科系資料
    List<StudentDataReportModel> getStudentAndDepartmentData();

    // 查詢學生考試成績資料
    List<StudentCourseScoreReportModel> getStudentCourseScoreData();

    // 整理科系平均成績 主報表、子報表資料
    List<DepartmentCourseScoreAverageReportModel> getDepartmentAverageScore();

    // 查詢學生考試成績資料 by date
    List<StudentCourseScoreReportModel> getStudentTestDataByDate(DatePeriodVo datePeriodVO);
}
