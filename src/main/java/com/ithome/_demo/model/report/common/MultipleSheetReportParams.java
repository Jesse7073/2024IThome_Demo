package com.ithome._demo.model.report.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultipleSheetReportParams {
    // 報表路徑與資料 key:路徑 value:資料
    private List<SheetReportDetail> sheetReportDetailList;

    // 匯出檔案類型
    private String fileType;
}
