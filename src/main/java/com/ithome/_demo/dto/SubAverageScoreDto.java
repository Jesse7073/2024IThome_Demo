package com.ithome._demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubAverageScoreDto {
    private Integer departmentId;

    private Integer courseId;

    private String courseDesc;

    private BigDecimal averageScore;
}
