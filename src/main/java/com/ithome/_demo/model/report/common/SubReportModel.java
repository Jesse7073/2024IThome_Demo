package com.ithome._demo.model.report.common;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SubReportModel {
    // 子報表參數
    private Map<String, Object> subReportParametersMap;

    // 子報表參數路徑
    private String subParametersMapExpression;

    // 子報表對應主報表資料路徑
    private String subDataExpression;

    // 子報表的資料來源
    private List subDataSource;

    // 子報表的資料來源路徑
    private String subDataSourceExpression;

    // 子報表檔案
    private String subJrxmlFilePath;
}
