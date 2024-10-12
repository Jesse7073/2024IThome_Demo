package com.ithome._demo.facade;

import com.ithome._demo.model.report.common.CommonReportModel;

public interface IWorkbookDemoFacade {
    // 匯出XSSF demo報表
    void exportXSSFExcel(int columnNumber, int rowNumber);

    // 匯出SXSSF demo報表
    void exportSXSSFExcel(int columnNumber, int rowNumber);

    // 匯出excel demo報表
    CommonReportModel demoExcel();

    // 讀取Excel demo報表
    void readExcel(String filePath);
}
