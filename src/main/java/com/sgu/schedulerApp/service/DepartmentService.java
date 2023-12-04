package com.sgu.schedulerApp.service;

import com.sgu.schedulerApp.dto.DepartmentDto;

import java.util.List;

public interface DepartmentService {
    List<DepartmentDto> findAll();

    List<DepartmentDto> findAllWithCodeOptional(String departmentCode);

    DepartmentDto findById(int id);

    DepartmentDto saveDepartment(DepartmentDto departmentDto);

    void deleteDepartment(int id);
}
