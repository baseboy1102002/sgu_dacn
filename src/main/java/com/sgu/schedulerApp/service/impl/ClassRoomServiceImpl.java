package com.sgu.schedulerApp.service.impl;

import com.sgu.schedulerApp.dto.ClassDto;
import com.sgu.schedulerApp.entity.Classroom;
import com.sgu.schedulerApp.repository.ClassroomRepository;
import com.sgu.schedulerApp.service.ClassRoomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassRoomServiceImpl implements ClassRoomService {

    private final ClassroomRepository classroomRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ClassDto> findAll() {
        List<Classroom> classrooms = classroomRepository.findByStatusTrue();
        return classrooms.stream().map(classroom -> modelMapper.map(classroom, ClassDto.class))
                .collect(Collectors.toList());
    }
}
