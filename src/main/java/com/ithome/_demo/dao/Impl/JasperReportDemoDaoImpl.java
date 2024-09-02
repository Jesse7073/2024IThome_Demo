package com.ithome._demo.dao.Impl;

import com.ithome._demo.dao.IJasperReportDemoDao;
import com.ithome._demo.dto.StudentAndDepartmentDto;
import com.ithome._demo.dto.StudentCourseScoreDto;
import com.ithome._demo.entity.QCourseEntity;
import com.ithome._demo.entity.QDepartmentEntity;
import com.ithome._demo.entity.QScoreEntity;
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
    public List<StudentAndDepartmentDto> getStudentAndDepartmentData() {
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

    @Override
    public List<StudentCourseScoreDto> getStudentCourseScoreData() {
        QStudentEntity qStudent = QStudentEntity.studentEntity;
        QDepartmentEntity qDepartment = QDepartmentEntity.departmentEntity;
        QCourseEntity qCourseEntity = QCourseEntity.courseEntity;
        QScoreEntity qScoreEntity = QScoreEntity.scoreEntity;

        return queryFactory.select(Projections.bean(StudentCourseScoreDto.class,
                    qStudent.studentId, qStudent.firstName, qStudent.lastName, qStudent.gender, qStudent.grade,
                    qDepartment.departmentId, qDepartment.departmentName, qDepartment.departmentDesc,
                    qCourseEntity.courseId, qCourseEntity.courseName, qCourseEntity.courseDesc, qCourseEntity.credit,
                    qScoreEntity.scoreId, qScoreEntity.score, qScoreEntity.testDate))
                .from(qStudent)
                .innerJoin(qDepartment).on(qStudent.departmentId.eq(qDepartment.departmentId))
                .innerJoin(qScoreEntity).on(qStudent.studentId.eq(qScoreEntity.studentId))
                .innerJoin(qCourseEntity).on(qScoreEntity.courseId.eq(qCourseEntity.courseId))
                .orderBy(qStudent.departmentId.asc(), qStudent.grade.asc(), qStudent.studentId.asc())
                .fetch();
    }
}
