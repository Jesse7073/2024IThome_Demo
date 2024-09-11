package com.ithome._demo.facade.Impl;

import com.ithome._demo.facade.IChartDemoFacade;
import com.ithome._demo.service.IReportDemoService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@Component
public class ChartDemoFacadeImpl implements IChartDemoFacade {
    @Autowired
    private IReportDemoService reportDemoService;

    /**
     * 匯出直條圖
     * */
    @Override
    public void exportBarChart() {
        // 1. 建立資料
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(15.0, "小明", "Q1");
        dataset.addValue(20.0, "小明", "Q2");
        dataset.addValue(12.0, "小明", "Q3");
        dataset.addValue(8.0, "小明", "Q4");
        dataset.addValue(5.0, "小華", "Q1");
        dataset.addValue(11.0, "小華", "Q2");
        dataset.addValue(16.0, "小華", "Q3");
        dataset.addValue(25.0, "小華", "Q4");

        // 2. 設定圖表主題
        StandardChartTheme standardChartTheme = new StandardChartTheme("myTheme"); // 建立主題
        standardChartTheme.setExtraLargeFont(new Font(Font.SERIF, Font.BOLD, 26)); // 標題字體
        standardChartTheme.setRegularFont(new Font(Font.SERIF, Font.BOLD, 14)); // 圖例字體
        standardChartTheme.setLargeFont(new Font(Font.SERIF, Font.BOLD, 18)); // 座標軸字體
//        standardChartTheme.setChartBackgroundPaint(Color.white);// 主題背景顏色
        ChartFactory.setChartTheme(standardChartTheme);

        // 2. 建立圖表
        JFreeChart chart = ChartFactory.createBarChart(
                "季度績效比較", // 圖表標題
                "季度", // 橫坐標標題
                "績效", // 縱座標標題
                dataset, // 資料
                PlotOrientation.VERTICAL, // 圖表的繪製方向，表示柱狀圖是垂直的；另一個選項是 PlotOrientation.HORIZONTAL，表示柱狀圖是水平的
                true, // 是否顯示圖例
                true, // 是否啟用工具提示
                false // 是否為圖表中的每個數據點生成對應的 URL(互動式圖表時使用)
        );

        CategoryPlot plot = (CategoryPlot)chart.getPlot(); // 取得圖表物件
        plot.setOutlineVisible(false);// 是否顯示邊框
        plot.setBackgroundPaint(Color.WHITE);// 背景顏色
        plot.setNoDataMessage("無資料");// 無資料時的提示
        plot.setNoDataMessageFont(new Font(Font.SERIF, Font.BOLD, 32));// 提示字體
        plot.setNoDataMessagePaint(Color.RED);// 提示字顏色
        plot.setRangeGridlinesVisible(false);// 是否顯示網格線 橫線

        // 圖例
        LegendTitle legend = chart.getLegend();// 取得圖例物件
        legend.setPosition(RectangleEdge.RIGHT);// 圖例位置(上、下、左、右)
        legend.setVisible(true);// 是否顯示圖例
        legend.setBorder(0, 0, 0, 0); // 圖例邊框粗细
        legend.setItemFont(new Font(Font.SERIF, Font.BOLD, 14));// 圖例字體大小

        // 設置 bar 顏色
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(156, 114, 243, 255)); // 設定系列 0（小明）的 bar 顏色
        renderer.setSeriesPaint(1, new Color(87, 165, 232)); // 設定系列 1（小華）的 bar 顏色
        renderer.setBarPainter(new StandardBarPainter()); // 設定標準的 bar 繪製方式，移除光影效果

        // 4. 匯出圖表
        try {
            String resourcePath = "C:/Users/j2093/Desktop/2024_ITHome_MockData/報表範例/JFreeChart";
            // 保存到本地(PNG或者JPEG格式)
            File f = new File(resourcePath + File.separator +"小明小華季度績效比較直條圖.png");
            ChartUtils.saveChartAsPNG(f ,chart, 800 ,400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
