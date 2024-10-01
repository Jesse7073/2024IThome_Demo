package com.ithome._demo.model.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCourseScoreAverageReportModel {
    private String departmentId;

    private String departmentDesc;

    // 子報表
    private List<SubReportAverageScoreModel> subReportAverageScoreModelList;
}
