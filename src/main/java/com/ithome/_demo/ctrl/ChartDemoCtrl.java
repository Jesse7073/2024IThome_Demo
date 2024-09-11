package com.ithome._demo.ctrl;

import com.ithome._demo.facade.IChartDemoFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequestMapping("/chart")
public class ChartDemoCtrl {
    @Autowired
    private IChartDemoFacade chartDemoFacade;

    /**
     * 下載直條圖 demo
     * */
    @GetMapping("/barChartImageDemo")
    public @ResponseBody void exportBarChart() {
        chartDemoFacade.exportBarChart();
    }
}
