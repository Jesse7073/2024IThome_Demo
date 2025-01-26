package com.ithome._demo.facade;

import com.ithome._demo.model.report.common.CommonChartModel;

public interface IChartDemoFacade {
    // 匯出直條圖
    void exportBarChart();

    // 各科系男女人數(直條圖插入JasperReport)
    CommonChartModel exportBarChartForJasper();
}
