package com.ithome._demo.dao;

import com.ithome._demo.entity.StudentEntity;

import java.util.List;

public interface IJasperReportDemoDao {
    // 查詢學生資料
    List<StudentEntity> queryStudentData();
}
