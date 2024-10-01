package com.ithome._demo.facade.Impl;

import com.ithome._demo.common.consts.FileType;
import com.ithome._demo.common.utils.DateUtil;
import com.ithome._demo.common.utils.ExportReportUtil;
import com.ithome._demo.facade.IJasperDemoFacade;
import com.ithome._demo.model.report.*;
import com.ithome._demo.model.report.common.CommonReportModel;
import com.ithome._demo.vo.DatePeriodVo;
import com.ithome._demo.model.report.common.SheetReportDetail;
import com.ithome._demo.service.IReportDemoService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JasperDemoFacadeImpl implements IJasperDemoFacade {
    @Autowired
    private IReportDemoService reportDemoService;

    /**
     * 下載學生與科系資料表(重複pageHeader)
     * */
    @Override
    public CommonReportModel exportStudentAndDepartmentDataReport() {
        // 1. 查詢學生基本資料
        List<StudentDataReportModel> studentDataReportModelList = reportDemoService.getStudentAndDepartmentData();

        // 2. 設定報表參數
        Map<String, Object> parametersMap = this.getStudentDataParameters(studentDataReportModelList);

        // 3.匯出excel byte[]
        byte[] bytes = null;
        String fileType = FileType.PDF;
        try {
            String reportPath = "/Report/Jasper/StudentDataReport.jrxml";
//            String[] sheetNames = new String[]{"學生與科系資料表"};
//
//            ExportReportParams params = new ExportReportParams(studentDataReportModelList, reportPath, parametersMap, fileType, sheetNames);

            // 4.1 匯出pdf JasperExportManager
            bytes = ExportReportUtil.templateToPdfByteSimple(studentDataReportModelList, reportPath, parametersMap);

            // 4.2 匯出pdf JRPdfExporter
            // 解決pdf中文無法顯示的問題
            // https://blog.csdn.net/zhouzhiwengang/article/details/90544340
            // https://blog.csdn.net/Mrqiang9001/article/details/113778799
//            bytes = ExportReportUtil.templateToPdfByte(studentDataReportModelList, reportPath, parametersMap);

            // 4.3 匯出指定檔案類型報表
//            bytes = ExportReportUtil.templateToByteByFileType(studentDataReportModelList, reportPath, parametersMap, fileType);

            // 4.4 匯出指定sheet名稱Excel報表
//            bytes = ExportReportUtil.templateToByteByFileType(params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 4.設定檔案名稱
        CommonReportModel commonReportModel = null;
        try {
            String encodedFilename = URLEncoder.encode("學生與科系資料." + fileType, StandardCharsets.UTF_8.name());
            commonReportModel = new CommonReportModel(bytes, encodedFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("下載 StudentDataExcelReport 成功");
        return commonReportModel;
    }

    /**
     * 下載學生課堂考試成績excel報表
     * sheet1 學生與科系資料表
     * sheet2 學生課堂考試成績
     * */
    @Override
    public CommonReportModel exportStudentCourseScoreDataReport() {
        // 1. 查詢學生基本資料
        List<StudentDataReportModel> studentDataReportModelList = reportDemoService.getStudentAndDepartmentData();

        // 2.查詢學生考試成績資料
        List<StudentCourseScoreReportModel> studentCourseScoreReportModelList = reportDemoService.getStudentCourseScoreData();

        // 3. 設定報表參數
        Map<String, Object> parametersMap = this.getDateParameters();

        // 4.匯出excel byte[]
        byte[] bytes = null;
        try {
            List<SheetReportDetail> sheetReportDetailList = getSheetReportDetailList(studentDataReportModelList, parametersMap, studentCourseScoreReportModelList);

            bytes = ExportReportUtil.templateToByteMultipleSheet(sheetReportDetailList);
            // 重複標題的問題
            // https://blog.csdn.net/daming924/article/details/7416613
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 6.設定檔案名稱
        String fileType = FileType.XLSX;
        CommonReportModel commonReportModel = null;
        try {
            String encodedFilename = URLEncoder.encode("學生課堂成績資料." + fileType, StandardCharsets.UTF_8.name());
            commonReportModel = new CommonReportModel(bytes, encodedFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("下載 StudentCourseScoreDataReport 成功");
        return commonReportModel;
    }

    /**
     * 下載學生與科系資料表(不重複pageHeader，加浮水印) pdf
     * */
    @Override
    public CommonReportModel exportStudentAndDepartmentDataMarkReport() {
        // 1. 查詢學生基本資料
        List<StudentDataReportModel> studentDataReportModelList = reportDemoService.getStudentAndDepartmentData();

        // 2. 設定報表參數
        Map<String, Object> parametersMap = new HashMap<>();
        LocalDate localDate = new Date().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        parametersMap.put("date", DateUtil.formatDate(localDate, DateUtil.DatePattern.DATE_NOTIFY));
        parametersMap.put("studentNum", studentDataReportModelList.size());

        InputStream waterMarkPath = getClass().getResourceAsStream("/static/images/iconmonstr-school-20%.png");
        parametersMap.put("waterMarkPath", waterMarkPath);

        // 4.匯出excel byte[]
        byte[] bytes = null;
        String fileType = FileType.PDF;
        try {
            String reportPath = "/Report/Jasper/StudentDataWithMarkReport.jrxml";
            // 4.2 匯出pdf JRPdfExporter
            bytes = ExportReportUtil.templateToPdfByte(studentDataReportModelList, reportPath, parametersMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 5.設定檔案名稱
        CommonReportModel commonReportModel = null;
        try {
            String encodedFilename = URLEncoder.encode("學生與科系資料." + fileType, StandardCharsets.UTF_8.name());
            commonReportModel = new CommonReportModel(bytes, encodedFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("下載 StudentDataExcelReport 成功");
        return commonReportModel;
    }

    /**
     * 下載學生科系考試平均成績excel報表(子報表)
     * */
    @Override
    public CommonReportModel exportDepartmentCourseScoreAverageDataReport() {
        // 1. 查詢各科系各課程考試平均成績資料
        List<DepartmentCourseScoreAverageReportModel> mainReportModelList = reportDemoService.getDepartmentAverageScore();

        // 2. 設定報表參數
        Map<String, Object> parametersMap = new HashMap<>();
        LocalDate localDate = new Date().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        parametersMap.put("date", localDate
                .format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));

        // 3. 設定子報表jasper文件
        String subReportPath = "/Report/Jasper/DepartmentCourseScoreAverageSubReport.jrxml";
        String expressionKey = "compiledSubReport";
        try {
            ExportReportUtil.setSubReportJasper(parametersMap, subReportPath, expressionKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 4. 子報表對應主報表
        this.mapSubReportData(mainReportModelList, parametersMap);

        // 5.匯出excel byte[]
        byte[] bytes = null;
        try {
            String reportPath = "/Report/Jasper/DepartmentCourseScoreAverageReport.jrxml";
            bytes = ExportReportUtil.templateToExcelByte(mainReportModelList, reportPath, parametersMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 6.設定檔案名稱
        CommonReportModel commonReportModel = null;
        String fileType = FileType.XLSX;
        try {
            String encodedFilename = URLEncoder.encode("科系課程平均成績表." + fileType, StandardCharsets.UTF_8.name());
            commonReportModel = new CommonReportModel(bytes, encodedFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("下載 DepartmentCourseScoreAverageDataReport 成功");
        return commonReportModel;
    }

    /**
     * 下載學生與科系資料表(不重複pageHeader，加圓餅圖) pdf
     * */
    @Override
    public CommonReportModel exportStudentAndDepartmentDataPieChartReport() {
        // 1. 查詢學生基本資料
        List<StudentDataReportModel> studentDataReportModelList = reportDemoService.getStudentAndDepartmentData();

        // 3. 計算科系人數
        List<DepartmentDataSourceModel> departmentDataSourceModelList = this.calDepartmentDataSource(studentDataReportModelList);

        // 4. 設定報表參數
        Map<String, Object> parametersMap = this.getStudentDataParameters(studentDataReportModelList);
        parametersMap.put("DepartmentNumDataset", new JRBeanCollectionDataSource(departmentDataSourceModelList));

        // 5.匯出excel byte[]
        byte[] bytes = null;
        String fileType = FileType.PDF;
        try {
            String reportPath = "/Report/Jasper/StudentDataWithPieChartReport.jrxml";
            // 4.2 匯出pdf JRPdfExporter
            bytes = ExportReportUtil.templateToPdfByte(studentDataReportModelList, reportPath, parametersMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 5.設定檔案名稱
        CommonReportModel commonReportModel = null;
        try {
            String encodedFilename = URLEncoder.encode("學生與科系資料(圓餅圖)." + fileType, StandardCharsets.UTF_8.name());
            commonReportModel = new CommonReportModel(bytes, encodedFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("下載 StudentDataExcelReport 成功");
        return commonReportModel;
    }

    // 購買烤肉用品統計表demo
    @Override
    public CommonReportModel exportBBQSuppliesDemoExcel() {
        // 1. 建立Demo資料
        List<BBQSuppliesModel> bbqSuppliesModelList = new ArrayList<>();
        BBQSuppliesModel bbqSuppliesModel1 = new BBQSuppliesModel("烤肉醬刷", 20, 3);
        BBQSuppliesModel bbqSuppliesModel2 = new BBQSuppliesModel("烤肉醬", 200, 2);
        BBQSuppliesModel bbqSuppliesModel3 = new BBQSuppliesModel("豬里肌", 150, 2);
        BBQSuppliesModel bbqSuppliesModel4 = new BBQSuppliesModel("雪花牛", 250, 1);
        BBQSuppliesModel bbqSuppliesModel5 = new BBQSuppliesModel("香菇", 120, 2);
        bbqSuppliesModelList.add(bbqSuppliesModel1);
        bbqSuppliesModelList.add(bbqSuppliesModel2);
        bbqSuppliesModelList.add(bbqSuppliesModel3);
        bbqSuppliesModelList.add(bbqSuppliesModel4);
        bbqSuppliesModelList.add(bbqSuppliesModel5);

        // 2. 設定報表參數
        Map<String, Object> parametersMap = new HashMap<>();
        // 第一個parameter：購買日期
        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        // 格式化購買日期 yyyy-MM-dd
        String formatDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        parametersMap.put("formatDate", formatDate);

        // 第二個parameter：計算金額總計
        Integer totalAmount = bbqSuppliesModelList.stream().mapToInt(BBQSuppliesModel::getPriceSum).sum();
        parametersMap.put("totalAmount", totalAmount);

        // 3.匯出excel byte[]
        String reportPath = "/Report/Jasper/demo.jrxml";
        byte[] bytes = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            // 以JasperCompileManager將jrxml模板編譯成jasper文件
            JasperReport jasperReport = JasperCompileManager.compileReport(JasperDemoFacadeImpl.class.getResourceAsStream(reportPath));

            // 將Java集合資料來源與Jasper報表進行綁定
            JRDataSource dataSource = new JRBeanCollectionDataSource(bbqSuppliesModelList);

            // 將資料填入報表
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parametersMap, dataSource);

            // 匯出XLSX
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
            exporter.exportReport();

            bytes = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 4.設定檔案名稱
        CommonReportModel commonReportModel = null;
        try {
            String encodedFilename = URLEncoder.encode("購買烤肉用品統計表demo.xlsx", StandardCharsets.UTF_8.name());
            commonReportModel = new CommonReportModel(bytes, encodedFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return commonReportModel;
    }

    // 匯出學生基本資料報表(Group)
    @Override
    public CommonReportModel exportStudentAndDepartmentGroupDataReport() {
        // 1. 查詢學生基本資料
        List<StudentDataReportModel> studentDataReportModelList = reportDemoService.getStudentAndDepartmentData();
        studentDataReportModelList = studentDataReportModelList.stream()
                .sorted(Comparator.comparing(StudentDataReportModel::getGrade))
                .collect(Collectors.toList());

        // 2. 設定報表參數
        Map<String, Object> parametersMap = new HashMap<>();
        LocalDate localDate = new Date().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        parametersMap.put("date", DateUtil.formatDate(localDate, DateUtil.DatePattern.DATE_NOTIFY));
        parametersMap.put("studentNum", studentDataReportModelList.size());

        InputStream waterMarkPath = getClass().getResourceAsStream("/static/images/iconmonstr-school-20%.png");
        parametersMap.put("waterMarkPath", waterMarkPath);

        // 4.匯出excel byte[]
        byte[] bytes = null;
        String fileType = FileType.PDF;
        try {
            String reportPath = "/Report/Jasper/StudentDataWithMarkGroupReport.jrxml";
            // 4.2 匯出pdf JRPdfExporter
            bytes = ExportReportUtil.templateToPdfByte(studentDataReportModelList, reportPath, parametersMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 5.設定檔案名稱
        CommonReportModel commonReportModel = null;
        try {
            String encodedFilename = URLEncoder.encode("學生與科系資料." + fileType, StandardCharsets.UTF_8.name());
            commonReportModel = new CommonReportModel(bytes, encodedFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("下載 StudentDataExcelReport 成功");
        return commonReportModel;
    }

    // 匯出學生成績資料表(變數：成績最高的)
    @Override
    public CommonReportModel exportStudentCourseScoreDataVariableReport() {
        // 1.查詢學生考試成績資料
        List<StudentCourseScoreReportModel> studentCourseScoreReportModelList = reportDemoService.getStudentCourseScoreData();

        // 2. 設定報表參數
        Map<String, Object> parametersMap = this.getDateParameters();

        // 3.匯出pdf byte[]
        byte[] bytes = null;
        String fileType = FileType.PDF;
        try {
            String reportPath = "/Report/Jasper/StudentCourseScoreReportVariable.jrxml";
            // 匯出pdf JRPdfExporter
            bytes = ExportReportUtil.templateToPdfByte(studentCourseScoreReportModelList, reportPath, parametersMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 4.設定檔案名稱
        CommonReportModel commonReportModel = null;
        try {
            String encodedFilename = URLEncoder.encode("學生考試成績資料." + fileType, StandardCharsets.UTF_8.name());
            commonReportModel = new CommonReportModel(bytes, encodedFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return commonReportModel;
    }

    // 匯出日期區間的學生成績資料表
    @Override
    public CommonReportModel exportStudentTestByDateReport(DatePeriodVo datePeriodVO) {
        // 1.查詢學生考試成績資料
        List<StudentCourseScoreReportModel> studentCourseScoreReportModelList = reportDemoService.getStudentTestDataByDate(datePeriodVO);

        // 2. 設定報表參數
        Map<String, Object> parametersMap = new HashMap<>();
        LocalDate startDate = datePeriodVO.getStartDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = datePeriodVO.getEndDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        parametersMap.put("startDate", startDate
                .format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
        parametersMap.put("endDate", endDate
                .format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));

        // 3. 匯出excel byte[]
        byte[] bytes = null;
        try {
            String reportPath = "/Report/Jasper/StudentTestByDateReport.jrxml";

            bytes = ExportReportUtil.templateToExcelByte(studentCourseScoreReportModelList, reportPath, parametersMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 4.設定檔案名稱
        CommonReportModel commonReportModel = null;
        String fileType = FileType.XLSX;
        try {
            String encodedFilename = URLEncoder.encode("學生考試成績資料." + fileType, StandardCharsets.UTF_8.name());
            commonReportModel = new CommonReportModel(bytes, encodedFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return commonReportModel;
    }

    // 匯出報價單
    @Override
    public CommonReportModel exportQuotation() {

        // 1.報價單資料
        QuotationModel quotationModel1 = new QuotationModel("原子習慣", 2, new BigDecimal(260));
        QuotationModel quotationModel2 = new QuotationModel("被討厭的勇氣", 1, new BigDecimal(300));
        QuotationModel quotationModel3 = new QuotationModel("阿德勒心理學講義", 10, new BigDecimal(268));
        QuotationModel quotationModel4 = new QuotationModel("新制多益TOEIC聽力／閱讀題庫解析", 1, new BigDecimal(1232));
        List<QuotationModel> quotationModelList = new ArrayList<>();
        quotationModelList.add(quotationModel1);
        quotationModelList.add(quotationModel2);
        quotationModelList.add(quotationModel3);
        quotationModelList.add(quotationModel4);

        // 2.設定報表參數
        Map<String, Object> parametersMap = new HashMap<>();
        LocalDate date = new Date().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        parametersMap.put("date", date
                .format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));

        // 3. 匯出excel byte[]
        byte[] bytes = null;
        try {
            String reportPath = "/Report/Jasper/StudentDataReportFormula.jrxml";

            bytes = ExportReportUtil.templateToExcelByte(quotationModelList, reportPath, parametersMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 4.設定檔案名稱
        CommonReportModel commonReportModel = null;
        String fileType = FileType.XLSX;
        try {
            String encodedFilename = URLEncoder.encode("書店報價單." + fileType, StandardCharsets.UTF_8.name());
            commonReportModel = new CommonReportModel(bytes, encodedFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return commonReportModel;
    }

    @Override
    public CommonReportModel exportExpenses() {

        // 1. 設定報表參數
        Map<String, Object> parametersMap = new HashMap<>();
        LocalDate date = new Date().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        parametersMap.put("date", date
                .format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));

        // 2. 匯出excel byte[]
        byte[] bytes = null;
        try {
            String reportPath = "/Report/Jasper/NoDataSourceReport.jrxml";
//            List<Integer> noDataSourceList = new ArrayList();
//
//            for (int i = 0; i <= 5 ; i++) {
//                noDataSourceList.add(i);
//            }

            bytes = ExportReportUtil.templateToExcelByte(null, reportPath, parametersMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 3.設定檔案名稱
        CommonReportModel commonReportModel = null;
        String fileType = FileType.XLSX;
        try {
            String encodedFilename = URLEncoder.encode("支出證明單." + fileType, StandardCharsets.UTF_8.name());
            commonReportModel = new CommonReportModel(bytes, encodedFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return commonReportModel;
    }

    private static List<SheetReportDetail> getSheetReportDetailList(List<StudentDataReportModel> studentDataReportModelList, Map<String, Object> parametersMap, List<StudentCourseScoreReportModel> studentCourseScoreReportModelList) {
        String reportPath1 = "/Report/Jasper/StudentDataReportExcel.jrxml";
        String reportPath2 = "/Report/Jasper/StudentCourseScoreReport.jrxml";
        String sheetName1 = "學生資料表";
        String sheetName2 = "學生考試成績表";
        List<SheetReportDetail> sheetReportDetailList = new ArrayList<>();
        SheetReportDetail sheetReportDetail1 = new SheetReportDetail(studentDataReportModelList, reportPath1, parametersMap, sheetName1);
        SheetReportDetail sheetReportDetail2 = new SheetReportDetail(studentCourseScoreReportModelList, reportPath2, parametersMap, sheetName2);
        sheetReportDetailList.add(sheetReportDetail1);
        sheetReportDetailList.add(sheetReportDetail2);
        return sheetReportDetailList;
    }

    private Map<String, Object> getStudentDataParameters(List<StudentDataReportModel> studentDataReportModelList) {
        Map<String, Object> parameters = new HashMap<>();
        LocalDate localDate = new Date().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        parameters.put("date", DateUtil.formatDate(localDate, DateUtil.DatePattern.DATE_NOTIFY));
        parameters.put("studentNum", studentDataReportModelList.size());

        return parameters;
    }

    private Map<String, Object> getDateParameters() {
        Map<String, Object> parameters = new HashMap<>();
        LocalDate localDate = DateUtil.toLocalDate(new Date());
        parameters.put("date", DateUtil.formatDate(localDate, DateUtil.DatePattern.DATE_NOTIFY));
        return parameters;
    }

    private void mapSubReportData(List<DepartmentCourseScoreAverageReportModel> mainReportModelList, Map<String, Object> parametersMap) {
        for (DepartmentCourseScoreAverageReportModel mainReportModel : mainReportModelList) {
            String departmentId = mainReportModel.getDepartmentId();
            List<SubReportAverageScoreModel> subReportModelList = mainReportModel.getSubReportAverageScoreModelList();
            // 設置為子報表的資料來源
//            if (departmentId.equals("3")) {
//                subReportModelList = new ArrayList<>();
//            }
            // 設置為子報表的資料來源
            JRDataSource subReportDataSource = new JRBeanCollectionDataSource(subReportModelList);
            parametersMap.put(departmentId, subReportDataSource);
        }
    }

    private List<DepartmentDataSourceModel> calDepartmentDataSource(List<StudentDataReportModel> studentDataReportModelList) {
        List<DepartmentDataSourceModel> departmentDataSourceModelList = new ArrayList<>();

        // 共幾位學生
        Integer studentNum = studentDataReportModelList.size();

        // 依照科系分群
        Map<String, List<StudentDataReportModel>> departmentMap = studentDataReportModelList.stream()
                .collect(Collectors.groupingBy(StudentDataReportModel::getDepartmentDesc));

        for (Map.Entry<String, List<StudentDataReportModel>> entry : departmentMap.entrySet()) {
            String departmentDesc = entry.getKey();
            List<StudentDataReportModel> entryValue = entry.getValue();
            Integer departmentTotalNum = entryValue.size();
            // 計算科系占比
            BigDecimal departmentRatio = BigDecimal.valueOf(departmentTotalNum)
                    .divide(BigDecimal.valueOf(studentNum), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));

            DepartmentDataSourceModel departmentDataSourceModel = new DepartmentDataSourceModel();
            departmentDataSourceModel.setDepartmentDesc(departmentDesc);
            departmentDataSourceModel.setDepartmentTotalNum(departmentTotalNum);
            departmentDataSourceModel.setDepartmentRatio(departmentRatio);
            departmentDataSourceModelList.add(departmentDataSourceModel);
        }
        return departmentDataSourceModelList;
    }
}
