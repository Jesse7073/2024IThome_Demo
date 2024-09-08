package com.ithome._demo.facade;

import com.ithome._demo.model.report.common.CommonReportModel;

public interface IJasperDemoFacade {
    // 匯出學生基本資料報表
    CommonReportModel exportStudentAndDepartmentDataReport();

    // 匯出學生課堂考試成績excel報表
    CommonReportModel exportStudentCourseScoreDataReport();

    // 下載學生與科系資料表(不重複pageHeader，加浮水印) pdf
    CommonReportModel exportStudentAndDepartmentDataMarkReport();

//    //下載學生科系考試平均成績excel報表
//    CommonReportModel exportDepartmentCourseScoreAverageDataReport();
}
