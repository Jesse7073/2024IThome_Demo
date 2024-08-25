package com.ithome._demo.service.Impl;

import com.ithome._demo.dao.IJasperReportDemoDao;
import com.ithome._demo.dto.StudentDto;
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
    public List<StudentDto> getStudentData() {
        List<StudentDto> studentDtoList = null;
        try {
            studentDtoList = Optional.of(jasperReportDemoDao.queryStudentData())
                    .orElse(new ArrayList<>())
                    .stream().map(StudentDto::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return studentDtoList;
    }
}
