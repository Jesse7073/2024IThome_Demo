package com.ithome._demo.model.report;

import lombok.Data;

@Data
public class BBQSuppliesModel {
    // 商品名稱
    private String productName;

    // 商品金額
    private Integer productPrice;

    // 商品數量
    private Integer productNum;

    // 購買金額小記
    private Integer priceSum;

    public BBQSuppliesModel(String productName, Integer productPrice, Integer productNum) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productNum = productNum;
        this.priceSum = productPrice * productNum;
    }
}
