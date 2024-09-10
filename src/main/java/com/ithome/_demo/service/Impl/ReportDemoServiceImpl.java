package com.ithome._demo.service.Impl;

import com.ithome._demo.dao.IJasperReportDemoDao;
import com.ithome._demo.dto.DepartmentAverageScoreDto;
import com.ithome._demo.dto.StudentAndDepartmentDto;
import com.ithome._demo.dto.StudentCourseScoreDto;
import com.ithome._demo.dto.SubAverageScoreDto;
import com.ithome._demo.service.IReportDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportDemoServiceImpl implements IReportDemoService {
    @Autowired
    private IJasperReportDemoDao jasperReportDemoDao;

    @Override
    public List<StudentAndDepartmentDto> getStudentAndDepartmentData() {
        List<StudentAndDepartmentDto> studentAndDepartmentDtoList = null;
        try {
            studentAndDepartmentDtoList = Optional.of(jasperReportDemoDao.getStudentAndDepartmentData())
                    .orElse(new ArrayList<>());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return studentAndDepartmentDtoList;
    }

    @Override
    public List<StudentCourseScoreDto> getStudentCourseScoreData() {
        List<StudentCourseScoreDto> studentCourseScoreDtoList = null;

        try {
            studentCourseScoreDtoList = Optional.of(jasperReportDemoDao.getStudentCourseScoreData())
                    .orElse(new ArrayList<>());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return studentCourseScoreDtoList;
    }

    @Override
    public List<DepartmentAverageScoreDto> getDepartmentAverageScore() {
        // 1.取得學生課程成績資料
        List<StudentCourseScoreDto> studentCourseScoreDtoList = null;

        try {
            studentCourseScoreDtoList = Optional.of(jasperReportDemoDao.getStudentCourseScoreData())
                    .orElse(new ArrayList<>());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 2.用科系分群
        Map<Integer, List<StudentCourseScoreDto>> studentCourseScoreDtoMap = studentCourseScoreDtoList.stream()
                .collect(Collectors.groupingBy(StudentCourseScoreDto::getDepartmentId));

        // 3.計算科系課程平均
        // 主報表、子報表物件清單
        List<DepartmentAverageScoreDto> departmentAverageScoreDtoList = new ArrayList<>();

        for (Map.Entry<Integer, List<StudentCourseScoreDto>> entry : studentCourseScoreDtoMap.entrySet()) {
            Integer departmentId = entry.getKey();
            List<StudentCourseScoreDto> entryValue = entry.getValue();
            String departmentDesc = entryValue.get(0).getDepartmentDesc();

            // 用課程分群
            Map<Integer, List<StudentCourseScoreDto>> courseScoreDtoMap = entryValue.stream()
                    .collect(Collectors.groupingBy(StudentCourseScoreDto::getCourseId));

            // 子報表清單
            List<SubAverageScoreDto> subAverageScoreDtoList = new ArrayList<>();
            // 計算課程平均
            for (Map.Entry<Integer, List<StudentCourseScoreDto>> courseEntry : courseScoreDtoMap.entrySet()) {
                Integer courseId = courseEntry.getKey();
                List<StudentCourseScoreDto> courseValue = courseEntry.getValue();
                String courseDesc = courseValue.get(0).getCourseDesc();
                BigDecimal totalScore = new BigDecimal(courseValue.stream().mapToInt(StudentCourseScoreDto::getScore).sum());
                BigDecimal averageScore = totalScore.divide(new BigDecimal(courseValue.size()), 2, RoundingMode.HALF_UP);

                // 子報表物件
                SubAverageScoreDto subAverageScoreDto = new SubAverageScoreDto(departmentId, courseId, courseDesc, averageScore);
                subAverageScoreDtoList.add(subAverageScoreDto);
            }

            // 主報表物件
            DepartmentAverageScoreDto departmentAverageScoreDto = new DepartmentAverageScoreDto();
            departmentAverageScoreDto.setDepartmentId(departmentId);
            departmentAverageScoreDto.setDepartmentDesc(departmentDesc);
            departmentAverageScoreDto.setSubAverageScoreDtoList(subAverageScoreDtoList);
            departmentAverageScoreDtoList.add(departmentAverageScoreDto);
        }

        return departmentAverageScoreDtoList;
    }
}
