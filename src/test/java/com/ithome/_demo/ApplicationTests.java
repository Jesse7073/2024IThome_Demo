package com.ithome._demo;

import com.ithome._demo.dto.StudentAndDepartmentDto;
import com.ithome._demo.service.IJasperReportDemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ApplicationTests {
	@Autowired
	private IJasperReportDemoService jasperReportDemoService;

	@Test
	void contextLoads() {
	}

	@Test
	public void getStudentDataTest() {
		List<StudentAndDepartmentDto> studentAndDepartmentDtoList = jasperReportDemoService.getStudentAndDepartmentData();
		System.out.println(studentAndDepartmentDtoList);
	}
}
