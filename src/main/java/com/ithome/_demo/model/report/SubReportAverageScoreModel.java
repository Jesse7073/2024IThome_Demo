package com.ithome._demo.model.report;

import com.ithome._demo.dto.SubAverageScoreDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubReportAverageScoreModel {
    private String courseDesc;

    private BigDecimal averageScore;
}
