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

    private Integer departmentId;

    private Integer courseId;

    private String courseDesc;

    private BigDecimal averageScore;

    public SubReportAverageScoreModel(SubAverageScoreDto dto) {
        this.departmentId = dto.getDepartmentId();
        this.courseId = dto.getCourseId();
        this.courseDesc = dto.getCourseDesc();
        this.averageScore = dto.getAverageScore();
    }
}
