package com.ithome._demo.dao.Impl;

import com.ithome._demo.dao.IJasperReportDemoDao;
import com.ithome._demo.dto.StudentAndDepartmentDto;
import com.ithome._demo.entity.QDepartmentEntity;
import com.ithome._demo.entity.QStudentEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JasperReportDemoDaoImpl implements IJasperReportDemoDao {

    @Autowired
    private JPQLQueryFactory queryFactory;

    @Override
    public List<StudentAndDepartmentDto> queryStudentData() {
        QStudentEntity qStudent = QStudentEntity.studentEntity;
        QDepartmentEntity qDepartment = QDepartmentEntity.departmentEntity;

        return queryFactory.select(Projections.bean(StudentAndDepartmentDto.class,
                        qStudent.studentId, qStudent.firstName, qStudent.lastName, qStudent.gender, qStudent.grade,
                        qDepartment.departmentId, qDepartment.departmentName, qDepartment.departmentDesc))
                        .from(qStudent)
                .innerJoin(qDepartment)
                .on(qStudent.departmentId.eq(qDepartment.departmentId))
                .fetch();
    }
}
