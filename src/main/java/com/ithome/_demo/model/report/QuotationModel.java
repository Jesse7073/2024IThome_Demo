package com.ithome._demo.model.report;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class QuotationModel {
    private String item;

    private Integer quantity;

    private BigDecimal price;
}
