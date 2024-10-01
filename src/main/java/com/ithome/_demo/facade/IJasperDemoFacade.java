package com.ithome._demo.facade;

import com.ithome._demo.model.report.common.CommonReportModel;
import com.ithome._demo.vo.DatePeriodVo;

public interface IJasperDemoFacade {
    // 匯出學生基本資料報表
    CommonReportModel exportStudentAndDepartmentDataReport();

    // 匯出學生課堂考試成績excel報表
    CommonReportModel exportStudentCourseScoreDataReport();

    // 下載學生與科系資料表(不重複pageHeader，加浮水印) pdf
    CommonReportModel exportStudentAndDepartmentDataMarkReport();

    // 下載學生科系考試平均成績excel報表
    CommonReportModel exportDepartmentCourseScoreAverageDataReport();

    // 下載學生科系比例圓餅圖
    CommonReportModel exportStudentAndDepartmentDataPieChartReport();

    // 購買烤肉用品統計表demo
    CommonReportModel exportBBQSuppliesDemoExcel();

    // 匯出學生基本資料報表(Group)
    CommonReportModel exportStudentAndDepartmentGroupDataReport();

    // 匯出學生成績資料表
    CommonReportModel exportStudentCourseScoreDataVariableReport();

    // 匯出日期區間的學生成績資料表
    CommonReportModel exportStudentTestByDateReport(DatePeriodVo datePeriodVO);

    // 匯出報價單
    CommonReportModel exportQuotation();

    // 匯出支出證明
    CommonReportModel exportExpenses();
}
