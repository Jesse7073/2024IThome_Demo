package com.ithome._demo.service.Impl;

import com.ithome._demo.dao.IJasperReportDemoDao;
import com.ithome._demo.dto.StudentAndDepartmentDto;
import com.ithome._demo.service.IJasperReportDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JasperReportDemoServiceImpl implements IJasperReportDemoService {
    @Autowired
    private IJasperReportDemoDao jasperReportDemoDao;

    @Override
    public List<StudentAndDepartmentDto> getStudentAndDepartmentData() {
        List<StudentAndDepartmentDto> studentAndDepartmentDtoList = null;
        try {
            studentAndDepartmentDtoList = Optional.of(jasperReportDemoDao.queryStudentData())
                    .orElse(new ArrayList<>());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return studentAndDepartmentDtoList;
    }
}
