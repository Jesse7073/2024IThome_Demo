package com.ithome._demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentAverageScoreDto {
    private Integer departmentId;

    private String departmentDesc;

    // 子報表
    private List<SubAverageScoreDto> subAverageScoreDtoList;
}
