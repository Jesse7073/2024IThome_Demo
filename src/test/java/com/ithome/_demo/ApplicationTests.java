package com.ithome._demo;

import com.ithome._demo.dto.StudentAndDepartmentDto;
import com.ithome._demo.service.IReportDemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ApplicationTests {
	@Autowired
	private IReportDemoService jasperReportDemoService;

	@Test
	void contextLoads() {
	}

	@Test
	public void getStudentDataTest() {
		List<StudentAndDepartmentDto> studentAndDepartmentDtoList = jasperReportDemoService.getStudentAndDepartmentData();
		System.out.println(studentAndDepartmentDtoList);
	}
}
