package com.ithome._demo.dao.Impl;

import com.ithome._demo.dao.IJasperReportDemoDao;
import com.ithome._demo.entity.QStudentEntity;
import com.ithome._demo.entity.StudentEntity;
import com.querydsl.jpa.JPQLQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JasperReportDemoDaoImpl implements IJasperReportDemoDao {

    @Autowired
    private JPQLQueryFactory queryFactory;

    @Override
    public List<StudentEntity> queryStudentData() {
        QStudentEntity qStudent = QStudentEntity.studentEntity;

        return queryFactory.selectFrom(qStudent).fetch();
    }
}
