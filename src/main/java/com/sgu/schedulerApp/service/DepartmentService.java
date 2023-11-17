package com.sgu.schedulerApp.service;

import com.sgu.schedulerApp.dto.DepartmentDto;

import java.util.List;

public interface DepartmentService {
    List<DepartmentDto> findAll();
}
