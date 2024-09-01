package com.ithome._demo.facade;

import com.ithome._demo.model.report.common.CommonReportModel;

public interface IJasperDemoFacade {
    // 匯出學生基本資料excel報表
    CommonReportModel exportStudentAndDepartmentDataReport();
}
