package com.sgu.schedulerApp.service.impl;

import com.sgu.schedulerApp.dto.ClassDto;
import com.sgu.schedulerApp.dto.RoomDto;
import com.sgu.schedulerApp.entity.Classroom;
import com.sgu.schedulerApp.entity.Room;
import com.sgu.schedulerApp.exception.CustomErrorException;
import com.sgu.schedulerApp.repository.ClassroomRepository;
import com.sgu.schedulerApp.repository.FacultyRepository;
import com.sgu.schedulerApp.service.ClassRoomService;
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
public class ClassRoomServiceImpl implements ClassRoomService {

    private final ClassroomRepository classroomRepository;
    private final FacultyRepository facultyRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ClassDto> findAll() {
        List<Classroom> classrooms = classroomRepository.findByStatusTrue();
        return classrooms.stream().map(classroom -> modelMapper.map(classroom, ClassDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassDto> findByFacultyCode(String keyword, String facultyCode) {
        keyword = keyword.isBlank() ? null:keyword;
        List<Classroom> classrooms = classroomRepository.findByKeywordAndDepartmentCode(keyword, facultyCode);
        return classrooms.stream().map(room -> modelMapper.map(room, ClassDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ClassDto findById(int id) {
        Optional<Classroom> classroom = classroomRepository.findById(id);
        if (classroom.isPresent()) {
            return modelMapper.map(classroom.get(), ClassDto.class);
        } else
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Không tìm thấy lớp với id:"+id);
    }

    @Override
    @Transactional
    public ClassDto saveClass(ClassDto classDto) {
        Optional<Classroom> optionalClass = classroomRepository.findById(classDto.getId());
        Classroom classroom;
        if (optionalClass.isPresent()) {
            classroom = optionalClass.get();
            if (checkClassHasChange(classroom, classDto)) {
                if (checkValidSaveClass(classDto))
                    throw new CustomErrorException(HttpStatus.NOT_ACCEPTABLE, "Mã Lớp đã tồn tại trong hệ thống.");
            }
        } else {
            classroom = new Classroom();
            if (checkValidSaveClass(classDto))
                throw new CustomErrorException(HttpStatus.NOT_ACCEPTABLE, "Mã Lớp đã tồn tại trong hệ thống.");
            classroom.setStatus(true);
        }
        classroom.setName(classDto.getName());
        classroom.setCode(classDto.getCode().toUpperCase());
        classroom.setFaculty(facultyRepository.findByCodeAndStatusTrue(classDto.getFacultyCode()));
        Classroom savedClass = classroomRepository.save(classroom);
        return modelMapper.map(savedClass, ClassDto.class);
    }

    private boolean checkValidSaveClass(ClassDto classDto) {
        return classroomRepository.existsByCodeAndStatusTrue(classDto.getCode());
    }

    private boolean checkClassHasChange(Classroom oldClass, ClassDto newClass) {
        return !oldClass.getCode().equals(newClass.getCode());
    }

    @Override
    @Transactional
    public void deleteClass(int id) {
        Optional<Classroom> classroom = classroomRepository.findById(id);
        if (classroom.isPresent()) {
            Classroom deletedClass = classroom.get();
            deletedClass.setStatus(false);
            classroomRepository.save(deletedClass);
        } else
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Không tìm thấy Lớp với id:"+id);
    }
}
