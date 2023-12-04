package com.sgu.schedulerApp.service;

import com.sgu.schedulerApp.dto.ClassDto;
import com.sgu.schedulerApp.dto.RoomDto;

import java.util.List;

public interface ClassRoomService {
    List<ClassDto> findAll();

    List<ClassDto> findByFacultyCode(String keyword, String departmentCode);

    ClassDto findById(int id);

    ClassDto saveClass(ClassDto classDto);

    void deleteClass(int id);
}
