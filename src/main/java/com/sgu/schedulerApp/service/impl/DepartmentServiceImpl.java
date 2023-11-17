package com.sgu.schedulerApp.service.impl;

import com.sgu.schedulerApp.dto.DepartmentDto;
import com.sgu.schedulerApp.entity.Department;
import com.sgu.schedulerApp.repository.DepartmentRepository;
import com.sgu.schedulerApp.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<DepartmentDto> findAll() {
        List<Department> departments = departmentRepository.findByStatusTrue();
        return departments.stream().map(department -> modelMapper.map(department, DepartmentDto.class))
                .collect(Collectors.toList());
    }
}
