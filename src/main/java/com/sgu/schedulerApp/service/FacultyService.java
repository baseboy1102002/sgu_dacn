package com.sgu.schedulerApp.service;

import com.sgu.schedulerApp.dto.FacultyDto;

import java.util.List;

public interface FacultyService {
    List<FacultyDto> findAll();
}
