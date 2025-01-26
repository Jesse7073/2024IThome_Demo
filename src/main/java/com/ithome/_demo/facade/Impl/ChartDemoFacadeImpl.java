package com.ithome._demo.facade.Impl;

import com.ithome._demo.common.utils.DateUtil;
import com.ithome._demo.common.utils.ExportReportUtil;
import com.ithome._demo.facade.IChartDemoFacade;
import com.ithome._demo.model.report.StudentDataReportModel;
import com.ithome._demo.model.report.common.CommonChartModel;
import com.ithome._demo.service.IReportDemoService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
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
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        dataset.addValue(71.304, "國文", "國文");
        dataset.addValue(69.72, "數學", "數學");
        dataset.addValue(69.76, "英文", "英文");
        dataset.addValue(69.488, "體育", "體育");

        // 2. 設定圖表主題
        StandardChartTheme standardChartTheme = new StandardChartTheme("myTheme"); // 建立主題
        standardChartTheme.setExtraLargeFont(new Font("微軟正黑體", Font.BOLD, 26)); // 標題字體
        standardChartTheme.setRegularFont(new Font("微軟正黑體", Font.BOLD, 14)); // 圖例字體
        standardChartTheme.setLargeFont(new Font("微軟正黑體", Font.BOLD, 18)); // 座標軸字體
//        standardChartTheme.setChartBackgroundPaint(Color.white);// 主題背景顏色
        ChartFactory.setChartTheme(standardChartTheme);

        // 2. 建立圖表
        JFreeChart chart = ChartFactory.createBarChart(
                "學生課程平均成績", // 圖表標題
                "課程", // 橫坐標標題
                "平均成績", // 縱座標標題
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
        plot.setNoDataMessageFont(new Font("微軟正黑體", Font.BOLD, 32));// 提示字體
        plot.setNoDataMessagePaint(Color.RED);// 提示字顏色
        plot.setRangeGridlinesVisible(false);// 是否顯示網格線 橫線

        // 調整 Y 軸刻度
        NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
        numberAxis.setTickUnit(new NumberTickUnit(5)); // 設定刻度單位為 10
        numberAxis.setRange(50, 90); // 設定 Y 軸範圍

        // 調整 X 軸顯示
        CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // 課程名稱傾斜 45 度顯示
        xAxis.setTickLabelFont(new Font("微軟正黑體", Font.BOLD, 14)); // 設定課程名稱字體大小

        // 圖例
        LegendTitle legend = chart.getLegend();// 取得圖例物件
        legend.setPosition(RectangleEdge.RIGHT); // 圖例位置
        legend.setVisible(true);// 是否顯示圖例
        legend.setBorder(0, 0, 0, 0); // 圖例邊框粗细
        legend.setItemFont(new Font("微軟正黑體", Font.BOLD, 14)); // 圖例字體大小

        // 設置 bar 顏色
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(156, 114, 243, 255));
        renderer.setSeriesPaint(1, new Color(87, 165, 232));
        renderer.setSeriesPaint(2, new Color(170, 232, 149));
        renderer.setSeriesPaint(3, new Color(222, 185, 144));
        // 設定標準的 bar 繪製方式，移除光影效果
        renderer.setBarPainter(new StandardBarPainter());
        // 設定 bar 的寬度為圖表寬度的 10%
        renderer.setMaximumBarWidth(0.1);

        // 4. 匯出圖表
        try {
            String resourcePath = "C:/Users/j2093/Desktop/2024_ITHome_MockData/報表範例/JFreeChart";
            // 保存到本地(PNG或者JPEG格式)
            File f = new File(resourcePath + File.separator +"學生課程平均成績直條圖.jpg");
            ChartUtils.saveChartAsPNG(f ,chart, 800 ,400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 各科系男女人數(直條圖插入JasperReport)
     * */
    @Override
    public CommonChartModel exportBarChartForJasper() {
        // 1. 查詢學生基本資料
        java.util.List<StudentDataReportModel> studentDataReportModelList = reportDemoService.getStudentAndDepartmentData();

        // 3. 設定報表參數
        Map<String, Object> parametersMap = this.getDateParameters();

        // 4.匯出excel byte[]
        byte[] bytes = null;
        try {
            String reportPath = "/Report/Jasper/StudentDataReport.jrxml";
            // 4.1 匯出pdf JasperExportManager
            bytes = ExportReportUtil.templateToPdfByteSimple(studentDataReportModelList, reportPath, parametersMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 5.計算各科系男女人數
        // 6.做各科系男女人數直條圖
        return null;
    }

    private Map<String, Object> getDateParameters() {
        Map<String, Object> parameters = new HashMap<>();
        LocalDate localDate = DateUtil.toLocalDate(new Date());
        parameters.put("date", DateUtil.formatDate(localDate, DateUtil.DatePattern.DATE_NOTIFY));
        return parameters;
    }
}
