package com.sgu.schedulerApp.service.impl;

import com.sgu.schedulerApp.entity.StudentInfo;
import com.sgu.schedulerApp.repository.StudentRepository;
import com.sgu.schedulerApp.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public StudentInfo findById(int id) {
        Optional<StudentInfo> studentInfo = studentRepository.findById(id);
        return studentInfo.get();
    }
}
