package com.ithome._demo.model.report;

import com.ithome._demo.dto.DepartmentAverageScoreDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCourseScoreAverageReportModel {
    private String departmentId;

    private String departmentDesc;

    // 子報表
    private List<SubReportAverageScoreModel> subReportAverageScoreModelList;

    public DepartmentCourseScoreAverageReportModel(DepartmentAverageScoreDto dto) {
        // 為了作為parametersMap的key，型別須轉為String
        this.departmentId = dto.getDepartmentId().toString();
        this.departmentDesc = dto.getDepartmentDesc();
        this.subReportAverageScoreModelList = dto.getSubAverageScoreDtoList().stream().map(SubReportAverageScoreModel::new).collect(Collectors.toList());
    }
}
