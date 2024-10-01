package com.ithome._demo.dao.Impl;

import com.ithome._demo.dao.IJasperReportDemoDao;
import com.ithome._demo.dto.DatePeriodDto;
import com.ithome._demo.dto.StudentAndDepartmentDto;
import com.ithome._demo.dto.StudentCourseScoreDto;
import com.ithome._demo.entity.QCourseEntity;
import com.ithome._demo.entity.QDepartmentEntity;
import com.ithome._demo.entity.QScoreEntity;
import com.ithome._demo.entity.QStudentEntity;
import com.querydsl.core.BooleanBuilder;
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
                .orderBy(qStudent.grade.asc(), qStudent.studentId.asc())
                .fetch();
    }

    @Override
    public List<StudentCourseScoreDto> getStudentCourseScoreData() {
        QStudentEntity qStudent = QStudentEntity.studentEntity;
        QDepartmentEntity qDepartment = QDepartmentEntity.departmentEntity;
        QCourseEntity qCourse = QCourseEntity.courseEntity;
        QScoreEntity qScore = QScoreEntity.scoreEntity;

        return queryFactory.select(Projections.bean(StudentCourseScoreDto.class,
                    qStudent.studentId, qStudent.firstName, qStudent.lastName, qStudent.gender, qStudent.grade,
                    qDepartment.departmentId, qDepartment.departmentName, qDepartment.departmentDesc,
                    qCourse.courseId, qCourse.courseName, qCourse.courseDesc, qCourse.credit,
                    qScore.scoreId, qScore.score, qScore.testDate))
                .from(qStudent)
                .innerJoin(qDepartment).on(qStudent.departmentId.eq(qDepartment.departmentId))
                .innerJoin(qScore).on(qStudent.studentId.eq(qScore.studentId))
                .innerJoin(qCourse).on(qScore.courseId.eq(qCourse.courseId))
                .orderBy(qCourse.courseDesc.asc(), qStudent.grade.asc(), qStudent.studentId.asc())
                .fetch();
    }

    @Override
    public List<StudentCourseScoreDto> getStudentTestDataByDate(DatePeriodDto datePeriodDto) {
        QStudentEntity qStudent = QStudentEntity.studentEntity;
        QDepartmentEntity qDepartment = QDepartmentEntity.departmentEntity;
        QCourseEntity qCourse = QCourseEntity.courseEntity;
        QScoreEntity qScore = QScoreEntity.scoreEntity;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qScore.testDate.between(datePeriodDto.getStartDate(), datePeriodDto.getEndDate()));

        return queryFactory.select(Projections.bean(StudentCourseScoreDto.class,
                        qStudent.studentId, qStudent.firstName, qStudent.lastName, qStudent.gender, qStudent.grade,
                        qDepartment.departmentId, qDepartment.departmentName, qDepartment.departmentDesc,
                        qCourse.courseId, qCourse.courseName, qCourse.courseDesc, qCourse.credit,
                        qScore.scoreId, qScore.score, qScore.testDate))
                .from(qStudent)
                .innerJoin(qDepartment).on(qStudent.departmentId.eq(qDepartment.departmentId))
                .innerJoin(qScore).on(qStudent.studentId.eq(qScore.studentId))
                .innerJoin(qCourse).on(qScore.courseId.eq(qCourse.courseId))
                .where(booleanBuilder)
                .orderBy(qCourse.courseDesc.asc(), qStudent.grade.asc(), qStudent.studentId.asc())
                .fetch();
    }
}
