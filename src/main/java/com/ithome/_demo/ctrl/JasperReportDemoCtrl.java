package com.ithome._demo.ctrl;

import com.ithome._demo.facade.IJasperDemoFacade;
import com.ithome._demo.model.report.common.CommonReportModel;
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
}
