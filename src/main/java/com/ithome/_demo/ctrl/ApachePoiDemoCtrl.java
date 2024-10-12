package com.ithome._demo.ctrl;

import com.ithome._demo.facade.IWorkbookDemoFacade;
import com.ithome._demo.model.report.common.CommonReportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequestMapping("/poi")
public class ApachePoiDemoCtrl {

    @Autowired
    private IWorkbookDemoFacade workbookDemoFacade;
    /**
     * 支出證明單(no dataSource)
     * */
    @GetMapping("/demoExcel")
    public @ResponseBody ResponseEntity<byte[]> demoExcel() {
        CommonReportModel commonReportModel = workbookDemoFacade.demoExcel();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename*=UTF-8''" + commonReportModel.getReportFileName())
                .body(commonReportModel.getReportBytes());
    }
}
