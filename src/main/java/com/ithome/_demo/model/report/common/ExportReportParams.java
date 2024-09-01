package com.ithome._demo.model.report.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportReportParams {
    // 報表資料
    private List dataSourceList;

    // 模板路徑
    private String reportPath;

    // 報表parametersMap
    private Map<String, Object> parametersMap;

    // 匯出檔案類型
    private String fileType;

    // excel的sheet名稱
    private String[] sheetNames;

}
