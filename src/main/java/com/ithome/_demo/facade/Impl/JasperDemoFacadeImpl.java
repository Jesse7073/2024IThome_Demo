package com.ithome._demo.facade.Impl;

import com.ithome._demo.common.consts.FileType;
import com.ithome._demo.common.utils.DateUtil;
import com.ithome._demo.common.utils.ExportReportUtil;
import com.ithome._demo.dto.StudentAndDepartmentDto;
import com.ithome._demo.facade.IJasperDemoFacade;
import com.ithome._demo.model.report.StudentDataReportModel;
import com.ithome._demo.model.report.common.CommonReportModel;
import com.ithome._demo.service.IJasperReportDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JasperDemoFacadeImpl implements IJasperDemoFacade {
    @Autowired
    private IJasperReportDemoService jasperReportDemoService;

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
        String reportPath = "/Report/Jasper/StudentDataReport.jrxml";
        String fileType = FileType.XLSX;
        try {
            // 4.1 匯出pdf JasperExportManager
//            bytes = ExportReportUtil.templateToPdfByteSimple(studentDataReportModelList, reportPath, parametersMap);

            // 4.2 匯出pdf JRPdfExporter
            // 解決pdf中文無法顯示的問題
            // https://blog.csdn.net/zhouzhiwengang/article/details/90544340
            // https://blog.csdn.net/Mrqiang9001/article/details/113778799
//            bytes = ExportReportUtil.templateToPdfByte(studentDataReportModelList, reportPath, parametersMap);

            // 4.3 匯出指定檔案類型報表
            bytes = ExportReportUtil.templateToByteByFileType(studentDataReportModelList, reportPath, parametersMap, fileType);
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

    private Map<String, Object> getStudentDataParameters(List<StudentDataReportModel> studentDataReportModelList) {
        Map<String, Object> parameters = new HashMap<>();
        LocalDate localDate = DateUtil.toLocalDate(new Date());
        parameters.put("date", DateUtil.formatDate(localDate, DateUtil.DatePattern.DATE_NOTIFY));
        parameters.put("studentNum", studentDataReportModelList.size());
        return parameters;
    }
}
