package com.ithome._demo.ctrl;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequestMapping("/chart")
public class ChartDemoCtrl {
}
