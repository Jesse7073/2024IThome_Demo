package com.ithome._demo.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "department", schema = "ithome2024")
public class DepartmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Department_Id")
    private Integer departmentId;

    @Column(name = "Department_Name")
    private String departmentName;

    @Column(name = "Department_Desc")
    private String departmentDesc;
}
