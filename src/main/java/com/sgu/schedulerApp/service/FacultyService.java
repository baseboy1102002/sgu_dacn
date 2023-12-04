package com.sgu.schedulerApp.service;

import com.sgu.schedulerApp.dto.FacultyDto;

import java.util.List;

public interface FacultyService {
    List<FacultyDto> findAll();

    List<FacultyDto> findAllWithCodeOptional(String facultyCode);

    FacultyDto findById(int id);

    FacultyDto saveFaculty(FacultyDto facultyDto);

    void deleteFaculty(int id);
}
