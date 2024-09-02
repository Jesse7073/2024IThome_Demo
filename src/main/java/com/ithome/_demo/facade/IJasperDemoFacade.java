package com.ithome._demo.facade;

import com.ithome._demo.model.report.common.CommonReportModel;

public interface IJasperDemoFacade {
    // 匯出學生基本資料報表
    CommonReportModel exportStudentAndDepartmentDataReport();

    // 匯出學生課堂考試成績excel報表
    CommonReportModel exportStudentCourseScoreDataReport();
}
