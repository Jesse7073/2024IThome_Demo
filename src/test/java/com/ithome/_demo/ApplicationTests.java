package com.ithome._demo;

import com.ithome._demo.dto.StudentAndDepartmentDto;
import com.ithome._demo.facade.IWorkbookDemoFacade;
import com.ithome._demo.service.IReportDemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ApplicationTests {
	@Autowired
	private IWorkbookDemoFacade workbookDemoFacade;

	@Test
	public void exportXSSFExcelTest() {
		workbookDemoFacade.exportXSSFExcel(10, 100000);
	}

	@Test
	public void exportSXSSFExcelTest() {
		workbookDemoFacade.exportSXSSFExcel(10, 100000);
	}

	@Test
	public void readExcel() {
		workbookDemoFacade.readExcel("C:\\Users\\j2093\\Desktop\\2024_ITHome_MockData\\學生考試成績表(加密).xlsx");
	}
}
