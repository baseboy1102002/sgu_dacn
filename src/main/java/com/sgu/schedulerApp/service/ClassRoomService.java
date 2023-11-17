package com.sgu.schedulerApp.service;

import com.sgu.schedulerApp.dto.ClassDto;

import java.util.List;

public interface ClassRoomService {
    List<ClassDto> findAll();
}
