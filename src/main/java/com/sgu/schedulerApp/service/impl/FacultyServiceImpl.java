package com.sgu.schedulerApp.service.impl;

import com.sgu.schedulerApp.dto.FacultyDto;
import com.sgu.schedulerApp.entity.Faculty;
import com.sgu.schedulerApp.repository.FacultyRepository;
import com.sgu.schedulerApp.service.FacultyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<FacultyDto> findAll() {
        List<Faculty> faculties = facultyRepository.findByStatusTrue();
        return faculties.stream().map(faculty -> modelMapper.map(faculty, FacultyDto.class))
                .collect(Collectors.toList());
    }
}
