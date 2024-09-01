package com.ithome._demo.model.report.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonReportModel {
    private byte[] reportBytes;

    private String reportFileName;
}
