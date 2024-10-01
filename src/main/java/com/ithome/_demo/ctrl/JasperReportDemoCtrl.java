package com.ithome._demo.ctrl;

import com.ithome._demo.facade.IJasperDemoFacade;
import com.ithome._demo.model.report.common.CommonReportModel;
import com.ithome._demo.vo.DatePeriodVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequestMapping("/jasper")
public class JasperReportDemoCtrl {
    @Autowired
    private IJasperDemoFacade jasperDemoFacade;

    /**
     * 下載學生與科系資料表(重複pageHeader)
     * */
    @GetMapping("/studentDataReport")
    public @ResponseBody ResponseEntity<byte[]> exportStudentDataReport() {
        CommonReportModel commonReportModel = jasperDemoFacade.exportStudentAndDepartmentDataReport();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename*=UTF-8''" + commonReportModel.getReportFileName())
                .body(commonReportModel.getReportBytes());
    }

    /**
     * 下載學生與科系資料表(不重複pageHeader，加浮水印) pdf
     * */
    @GetMapping("/studentAndDepartmentDataMarkReport")
    public @ResponseBody ResponseEntity<byte[]> exportStudentAndDepartmentDataMarkReport() {
        CommonReportModel commonReportModel = jasperDemoFacade.exportStudentAndDepartmentDataMarkReport();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename*=UTF-8''" + commonReportModel.getReportFileName())
                .body(commonReportModel.getReportBytes());
    }

    /**
     * 下載學生課堂考試成績excel報表
     * sheet1 學生與科系資料表
     * sheet2 學生課堂考試成績
     * */
    @GetMapping("/studentCourseScoreDataReport")
    public @ResponseBody ResponseEntity<byte[]> exportStudentCourseScoreDataReport() {
        CommonReportModel commonReportModel = jasperDemoFacade.exportStudentCourseScoreDataReport();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename*=UTF-8''" + commonReportModel.getReportFileName())
                .body(commonReportModel.getReportBytes());
    }

    /**
     * 下載學生科系考試平均成績excel報表
     * */
    @GetMapping("/departmentCourseScoreAverageDataReport")
    public @ResponseBody ResponseEntity<byte[]> exportDepartmentCourseScoreAverageDataReport() {
        CommonReportModel commonReportModel = jasperDemoFacade.exportDepartmentCourseScoreAverageDataReport();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename*=UTF-8''" + commonReportModel.getReportFileName())
                .body(commonReportModel.getReportBytes());
    }

    /**
     * 下載學生與科系資料表(不重複pageHeader，加圓餅圖) pdf
     * */
    @GetMapping("/studentAndDepartmentDataPieChartReport")
    public @ResponseBody ResponseEntity<byte[]> exportStudentAndDepartmentDataPieChartReport() {
        CommonReportModel commonReportModel = jasperDemoFacade.exportStudentAndDepartmentDataPieChartReport();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename*=UTF-8''" + commonReportModel.getReportFileName())
                .body(commonReportModel.getReportBytes());
    }

    /**
     * 購買烤肉用品統計表demo excel
     * */
    @GetMapping("/BBQSuppliesDemoExcel")
    public @ResponseBody ResponseEntity<byte[]> exportBBQSuppliesDemoExcel() {
        CommonReportModel commonReportModel = jasperDemoFacade.exportBBQSuppliesDemoExcel();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename*=UTF-8''" + commonReportModel.getReportFileName())
                .body(commonReportModel.getReportBytes());
    }

    /**
     * 匯出學生基本資料報表(Group)
     * */
    @GetMapping("/studentAndDepartmentGroupDataReport")
    public @ResponseBody ResponseEntity<byte[]> exportStudentAndDepartmentGroupDataReport() {
        CommonReportModel commonReportModel = jasperDemoFacade.exportStudentAndDepartmentGroupDataReport();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename*=UTF-8''" + commonReportModel.getReportFileName())
                .body(commonReportModel.getReportBytes());
    }

    /**
     * 匯出學生成績資料表(變數：成績最高的)
     * */
    @GetMapping("/studentCourseScoreDataVariableReport")
    public @ResponseBody ResponseEntity<byte[]> exportStudentCourseScoreData() {
        CommonReportModel commonReportModel = jasperDemoFacade.exportStudentCourseScoreDataVariableReport();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename*=UTF-8''" + commonReportModel.getReportFileName())
                .body(commonReportModel.getReportBytes());
    }

    /**
     * 匯出學生成績資料表(變數：成績最高的)
     * */
    @PostMapping("/studentTestByDateReport")
    public @ResponseBody ResponseEntity<byte[]> exportStudentTestByDateReport(@RequestBody DatePeriodVo datePeriodVO) {
        CommonReportModel commonReportModel = jasperDemoFacade.exportStudentTestByDateReport(datePeriodVO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename*=UTF-8''" + commonReportModel.getReportFileName())
                .body(commonReportModel.getReportBytes());
    }

    /**
     * 報價單(excel公式)
     * */
    @GetMapping("/quotation")
    public @ResponseBody ResponseEntity<byte[]> exportQuotation() {
        CommonReportModel commonReportModel = jasperDemoFacade.exportQuotation();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename*=UTF-8''" + commonReportModel.getReportFileName())
                .body(commonReportModel.getReportBytes());
    }

    /**
     * 支出證明單(no dataSource)
     * */
    @GetMapping("/expenses")
    public @ResponseBody ResponseEntity<byte[]> exportExpenses() {
        CommonReportModel commonReportModel = jasperDemoFacade.exportExpenses();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename*=UTF-8''" + commonReportModel.getReportFileName())
                .body(commonReportModel.getReportBytes());
    }
}
