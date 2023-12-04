package com.sgu.schedulerApp.service.impl;

import com.sgu.schedulerApp.dto.DepartmentDto;
import com.sgu.schedulerApp.dto.FacultyDto;
import com.sgu.schedulerApp.entity.Department;
import com.sgu.schedulerApp.entity.Faculty;
import com.sgu.schedulerApp.exception.CustomErrorException;
import com.sgu.schedulerApp.repository.FacultyRepository;
import com.sgu.schedulerApp.service.FacultyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    @Override
    public List<FacultyDto> findAllWithCodeOptional(String facultyCode) {
        facultyCode = facultyCode.isBlank() ? null:facultyCode;
        List<Faculty> faculties = facultyRepository.findByCodeOptionalAndStatusTrue(facultyCode);
        return faculties.stream().map(faculty -> modelMapper.map(faculty, FacultyDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public FacultyDto findById(int id) {
        Optional<Faculty> faculty = facultyRepository.findById(id);
        if (faculty.isPresent()) {
            return modelMapper.map(faculty.get(), FacultyDto.class);
        } else
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Không tìm thấy khoa với id:"+id);
    }

    @Override
    @Transactional
    public FacultyDto saveFaculty(FacultyDto facultyDto) {
        Optional<Faculty> optionalFaculty = facultyRepository.findById(facultyDto.getId());
        Faculty faculty;
        if (optionalFaculty.isPresent()) {
            faculty = optionalFaculty.get();
            if (checkFacultyHasChange(faculty, facultyDto)) {
                if (checkValidSaveFaculty(facultyDto))
                    throw new CustomErrorException(HttpStatus.NOT_ACCEPTABLE, "Mã khoa đã tồn tại trong hệ thống.");
            }
        } else {
            faculty = new Faculty();
            if (checkValidSaveFaculty(facultyDto))
                throw new CustomErrorException(HttpStatus.NOT_ACCEPTABLE, "Mã khoa đã tồn tại trong hệ thống.");
            faculty.setStatus(true);
        }
        faculty.setCode(facultyDto.getCode().toUpperCase());
        faculty.setName(facultyDto.getName());
        Faculty savedFaculty = facultyRepository.save(faculty);
        return modelMapper.map(savedFaculty, FacultyDto.class);
    }

    private boolean checkFacultyHasChange(Faculty oldF, FacultyDto newF) {
        if ( !oldF.getCode().equals(newF.getCode()) && !oldF.getName().equals(newF.getName()) ) {
            return facultyRepository.existsByCodeOrNameAndStatusTrue(newF.getCode(), newF.getName());
        }
        else if ( !oldF.getCode().equals(newF.getCode()) ) {
            return facultyRepository.existsByCodeAndStatusTrue(newF.getCode());
        } else if ( !oldF.getName().equals(newF.getName()) ) {
            return facultyRepository.existsByNameAndStatusTrue(newF.getName());
        }
        return false;
    }

    private boolean checkValidSaveFaculty(FacultyDto facultyDto) {
        return facultyRepository.existsByCodeOrNameAndStatusTrue(facultyDto.getCode(), facultyDto.getName());
    }

    @Override
    @Transactional
    public void deleteFaculty(int id) {
        Optional<Faculty> faculty = facultyRepository.findById(id);
        if (faculty.isPresent()) {
            Faculty deleteF = faculty.get();
            deleteF.setStatus(false);
            deleteF.getClassrooms().forEach(c->c.setStatus(false));
            facultyRepository.save(deleteF);
        } else
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Không tìm thấy khoa với id:"+id);
    }
}
