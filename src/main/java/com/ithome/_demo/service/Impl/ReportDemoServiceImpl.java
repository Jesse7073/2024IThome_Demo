package com.ithome._demo.service.Impl;

import com.ithome._demo.dao.IJasperReportDemoDao;
import com.ithome._demo.dto.DatePeriodDto;
import com.ithome._demo.dto.DepartmentAverageScoreDto;
import com.ithome._demo.dto.StudentCourseScoreDto;
import com.ithome._demo.dto.SubAverageScoreDto;
import com.ithome._demo.model.report.DepartmentCourseScoreAverageReportModel;
import com.ithome._demo.model.report.StudentCourseScoreReportModel;
import com.ithome._demo.model.report.StudentDataReportModel;
import com.ithome._demo.model.report.SubReportAverageScoreModel;
import com.ithome._demo.vo.DatePeriodVo;
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
    public List<StudentDataReportModel> getStudentAndDepartmentData() {
        List<StudentDataReportModel> studentDataReportModelList = null;
        try {
            studentDataReportModelList = Optional.of(jasperReportDemoDao.getStudentAndDepartmentData())
                    .orElse(new ArrayList<>())
                    .stream().map(StudentDataReportModel::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return studentDataReportModelList;
    }

    @Override
    public List<StudentCourseScoreReportModel> getStudentCourseScoreData() {
        List<StudentCourseScoreReportModel> studentCourseScoreReportModelList = null;

        try {
            studentCourseScoreReportModelList = Optional.of(jasperReportDemoDao.getStudentCourseScoreData())
                    .orElse(new ArrayList<>())
                    .stream().map(StudentCourseScoreReportModel::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return studentCourseScoreReportModelList;
    }

    @Override
    public List<DepartmentCourseScoreAverageReportModel> getDepartmentAverageScore() {
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
        List<DepartmentCourseScoreAverageReportModel> mainReportModelList = new ArrayList<>();

        for (Map.Entry<Integer, List<StudentCourseScoreDto>> entry : studentCourseScoreDtoMap.entrySet()) {
            Integer departmentId = entry.getKey();
            List<StudentCourseScoreDto> entryValue = entry.getValue();
            String departmentDesc = entryValue.get(0).getDepartmentDesc();

            // 用課程分群
            Map<Integer, List<StudentCourseScoreDto>> courseScoreDtoMap = entryValue.stream()
                    .collect(Collectors.groupingBy(StudentCourseScoreDto::getCourseId));

            // 子報表清單
            List<SubReportAverageScoreModel> subReportModelList = new ArrayList<>();
            // 計算課程平均
            for (Map.Entry<Integer, List<StudentCourseScoreDto>> courseEntry : courseScoreDtoMap.entrySet()) {
                List<StudentCourseScoreDto> courseValue = courseEntry.getValue();
                String courseDesc = courseValue.get(0).getCourseDesc();
                BigDecimal totalScore = new BigDecimal(courseValue.stream().mapToInt(StudentCourseScoreDto::getScore).sum());
                BigDecimal averageScore = totalScore.divide(new BigDecimal(courseValue.size()), 2, RoundingMode.HALF_UP);

                // 課程名稱、平均成績放入子報表物件
                SubReportAverageScoreModel subReportAverageScoreModel = new SubReportAverageScoreModel(courseDesc, averageScore);
                subReportModelList.add(subReportAverageScoreModel);
            }

            // 主報表物件
            DepartmentCourseScoreAverageReportModel mainReportModel = new DepartmentCourseScoreAverageReportModel();
            // 為了作為parametersMap的key，型別須轉為String
            mainReportModel.setDepartmentId(departmentId.toString());
            mainReportModel.setDepartmentDesc(departmentDesc);
            mainReportModel.setSubReportAverageScoreModelList(subReportModelList);
            mainReportModelList.add(mainReportModel);
        }

        return mainReportModelList;
    }

    // 查詢學生考試成績資料 by date
    @Override
    public List<StudentCourseScoreReportModel> getStudentTestDataByDate(DatePeriodVo datePeriodVO) {
        DatePeriodDto datePeriodDto = datePeriodVO.toDatePeriodDto();

        List<StudentCourseScoreReportModel> studentCourseScoreReportModelList = null;

        try {
            studentCourseScoreReportModelList = Optional.of(jasperReportDemoDao.getStudentTestDataByDate(datePeriodDto))
                    .orElse(new ArrayList<>())
                    .stream().map(StudentCourseScoreReportModel::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return studentCourseScoreReportModelList;
    }
}
