package com.ithome._demo.model.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDataSourceModel {
    private String departmentDesc;

    private Integer departmentTotalNum;

    private BigDecimal departmentRatio;
}
