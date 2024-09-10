package com.ithome._demo.facade.Impl;

import com.ithome._demo.common.consts.FileType;
import com.ithome._demo.common.utils.DateUtil;
import com.ithome._demo.common.utils.ExportReportUtil;
import com.ithome._demo.dto.DepartmentAverageScoreDto;
import com.ithome._demo.dto.StudentAndDepartmentDto;
import com.ithome._demo.dto.StudentCourseScoreDto;
import com.ithome._demo.facade.IJasperDemoFacade;
import com.ithome._demo.model.report.*;
import com.ithome._demo.model.report.common.*;
import com.ithome._demo.service.IReportDemoService;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JasperDemoFacadeImpl implements IJasperDemoFacade {
    @Autowired
    private IReportDemoService jasperReportDemoService;

    /**
     * 下載學生與科系資料表(重複pageHeader)
     * */
    @Override
    public CommonReportModel exportStudentAndDepartmentDataReport() {
        // 1. 查詢學生基本資料
        List<StudentAndDepartmentDto> studentAndDepartmentDtoList = jasperReportDemoService.getStudentAndDepartmentData();

        // 2. 轉換為報表資料
        List<StudentDataReportModel> studentDataReportModelList = Optional.of(studentAndDepartmentDtoList)
                .orElse(new ArrayList<>()).stream()
                .map(StudentAndDepartmentDto::toStudentDataReportModel).collect(Collectors.toList());

        // 3. 設定報表參數
        Map<String, Object> parametersMap = this.getStudentDataParameters(studentDataReportModelList);

        // 4.匯出excel byte[]
        byte[] bytes = null;
        String fileType = FileType.XLSX;
        try {
            String reportPath = "/Report/Jasper/StudentDataReport.jrxml";
            String[] sheetNames = new String[]{"學生與科系資料表"};

            ExportReportParams params = new ExportReportParams(studentDataReportModelList, reportPath, parametersMap, fileType, sheetNames);

            // 4.1 匯出pdf JasperExportManager
//            bytes = ExportReportUtil.templateToPdfByteSimple(studentDataReportModelList, reportPath, parametersMap);

            // 4.2 匯出pdf JRPdfExporter
            // 解決pdf中文無法顯示的問題
            // https://blog.csdn.net/zhouzhiwengang/article/details/90544340
            // https://blog.csdn.net/Mrqiang9001/article/details/113778799
//            bytes = ExportReportUtil.templateToPdfByte(studentDataReportModelList, reportPath, parametersMap);

            // 4.3 匯出指定檔案類型報表
//            bytes = ExportReportUtil.templateToByteByFileType(studentDataReportModelList, reportPath, parametersMap, fileType);

            // 4.4 匯出指定sheet名稱Excel報表
            bytes = ExportReportUtil.templateToByteByFileType(params);
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
     * 下載學生課堂考試成績excel報表
     * sheet1 學生與科系資料表
     * sheet2 學生課堂考試成績
     * */
    @Override
    public CommonReportModel exportStudentCourseScoreDataReport() {
        // 1. 查詢學生基本資料
        List<StudentAndDepartmentDto> studentAndDepartmentDtoList = jasperReportDemoService.getStudentAndDepartmentData();

        // 2.查詢學生考試成績資料
        List<StudentCourseScoreDto> studentCourseScoreDtoList = jasperReportDemoService.getStudentCourseScoreData();

        // 3. 轉換為報表資料
        List<StudentDataReportModel> studentDataReportModelList = Optional.of(studentAndDepartmentDtoList)
                .orElse(new ArrayList<>()).stream()
                .map(StudentAndDepartmentDto::toStudentDataReportModel).collect(Collectors.toList());

        List<StudentCourseScoreReportModel> studentCourseScoreReportModelList = Optional.of(studentCourseScoreDtoList)
                .orElse(new ArrayList<>()).stream()
                .map(StudentCourseScoreDto::toStudentCourseScoreReportModel).collect(Collectors.toList());

        // 4. 設定報表參數
        Map<String, Object> parametersMap = this.getDateParameters();

        // 5.匯出excel byte[]
        byte[] bytes = null;
        String fileType = FileType.XLSX;
        try {
            List<SheetReportDetail> sheetReportDetailList = getSheetReportDetailList(studentDataReportModelList, parametersMap, studentCourseScoreReportModelList);

            MultipleSheetReportParams params = new MultipleSheetReportParams(sheetReportDetailList, fileType);

            bytes = ExportReportUtil.templateToByteMultipleSheet(params);
            // 重複標題的問題
            // https://blog.csdn.net/daming924/article/details/7416613
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 6.設定檔案名稱
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
        List<StudentAndDepartmentDto> studentAndDepartmentDtoList = jasperReportDemoService.getStudentAndDepartmentData();

        // 2. 轉換為報表資料
        List<StudentDataReportModel> studentDataReportModelList = Optional.of(studentAndDepartmentDtoList)
                .orElse(new ArrayList<>()).stream()
                .map(StudentAndDepartmentDto::toStudentDataReportModel).collect(Collectors.toList());

        // 3. 設定報表參數
        Map<String, Object> parametersMap = this.getStudentDataParameters(studentDataReportModelList);

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
     * 下載學生科系考試平均成績excel報表
     * */
    @Override
    public CommonReportModel exportDepartmentCourseScoreAverageDataReport() {
        // 1. 查詢各科系各課程考試平均成績資料
        List<DepartmentAverageScoreDto> departmentAverageScoreList = jasperReportDemoService.getDepartmentAverageScore();

        // 2. 轉換為報表資料
        List<DepartmentCourseScoreAverageReportModel> departmentCourseScoreAverageReportModelList = Optional.of(departmentAverageScoreList)
                .orElse(new ArrayList<>()).stream()
                .map(DepartmentCourseScoreAverageReportModel::new).collect(Collectors.toList());

        // 3. 設定報表參數
        Map<String, Object> parametersMap = this.getDateParameters();

        // 4. 子報表對應主報表
        mapSubReportData(departmentCourseScoreAverageReportModelList, parametersMap);

        // 5. 設定子報表來源
//        SubReportModel subReportModel = new SubReportModel();
//        subReportModel.setSubDataExpression(parametersMap);
//        subReportModel.setSubJrxmlFilePath("/Report/Jasper/DepartmentCourseScoreAverageSubReport.jrxml");
//        subReportModel.setSubDataSource();
//
//        ExportReportUtil.setSubReportSource(parametersMap, subReportModel);

        // 5.匯出excel byte[]
        byte[] bytes = null;
        String fileType = FileType.XLSX;
        try {
//            List<SheetReportDetail> sheetReportDetailList = getSheetReportDetailList(studentDataReportModelList, parametersMap, studentCourseScoreReportModelList);
//
//            MultipleSheetReportParams params = new MultipleSheetReportParams(sheetReportDetailList, fileType);
//
//            bytes = ExportReportUtil.templateToByteMultipleSheet(params);
            // 重複標題的問題
            // https://blog.csdn.net/daming924/article/details/7416613
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 6.設定檔案名稱
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
     * 下載學生與科系資料表(不重複pageHeader，加圓餅圖) pdf
     * */
    @Override
    public CommonReportModel exportStudentAndDepartmentDataPieChartReport() {
        // 1. 查詢學生基本資料
        List<StudentAndDepartmentDto> studentAndDepartmentDtoList = jasperReportDemoService.getStudentAndDepartmentData();

        // 2. 轉換為報表資料
        List<StudentDataReportModel> studentDataReportModelList = Optional.of(studentAndDepartmentDtoList)
                .orElse(new ArrayList<>()).stream()
                .map(StudentAndDepartmentDto::toStudentDataReportModel).collect(Collectors.toList());

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

    private static List<SheetReportDetail> getSheetReportDetailList(List<StudentDataReportModel> studentDataReportModelList, Map<String, Object> parametersMap, List<StudentCourseScoreReportModel> studentCourseScoreReportModelList) {
        String reportPath1 = "/Report/Jasper/StudentDataReport.jrxml";
        String reportPath2 = "/Report/Jasper/StudentCourseScoreReport.jrxml";
        String sheetName1 = "學生與科系資料表";
        String sheetName2 = "學生課堂考試成績表";
        List<SheetReportDetail> sheetReportDetailList = new ArrayList<>();
        SheetReportDetail sheetReportDetail1 = new SheetReportDetail(studentDataReportModelList, reportPath1, parametersMap, sheetName1);
        SheetReportDetail sheetReportDetail2 = new SheetReportDetail(studentCourseScoreReportModelList, reportPath2, parametersMap, sheetName2);
        sheetReportDetailList.add(sheetReportDetail1);
        sheetReportDetailList.add(sheetReportDetail2);
        return sheetReportDetailList;
    }

    private Map<String, Object> getStudentDataParameters(List<StudentDataReportModel> studentDataReportModelList) {
        Map<String, Object> parameters = new HashMap<>();
        LocalDate localDate = DateUtil.toLocalDate(new Date());
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

    private void mapSubReportData(List<DepartmentCourseScoreAverageReportModel> departmentCourseScoreAverageReportModelList, Map<String, Object> parametersMap) {
        for (DepartmentCourseScoreAverageReportModel departmentCourseScoreAverageReportModel : departmentCourseScoreAverageReportModelList) {
            List<SubReportAverageScoreModel> subReportAverageScoreModelList = departmentCourseScoreAverageReportModel.getSubReportAverageScoreModelList();
            parametersMap.put("departmentId", subReportAverageScoreModelList);
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
