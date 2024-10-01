package com.ithome._demo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ithome._demo.dto.DatePeriodDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatePeriodVo {
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    public DatePeriodDto toDatePeriodDto() {
        DatePeriodDto datePeriodDto = new DatePeriodDto();
        datePeriodDto.setStartDate(startDate);
        datePeriodDto.setEndDate(endDate);
        return datePeriodDto;
    }
}
