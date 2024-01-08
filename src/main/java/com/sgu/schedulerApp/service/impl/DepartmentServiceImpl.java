package com.sgu.schedulerApp.service.impl;

import com.sgu.schedulerApp.dto.DepartmentDto;
import com.sgu.schedulerApp.entity.Department;
import com.sgu.schedulerApp.exception.CustomErrorException;
import com.sgu.schedulerApp.repository.DepartmentRepository;
import com.sgu.schedulerApp.service.DepartmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
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

    @Override
    public List<DepartmentDto> findAllWithCodeOptional(String departmentCode) {
        departmentCode = departmentCode.isBlank() ? null:departmentCode;
        List<Department> departments = departmentRepository.findByCodeOptionalAndStatusTrue(departmentCode);
        return departments.stream().map(department -> modelMapper.map(department, DepartmentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDto findById(int id) {
        Optional<Department> department = departmentRepository.findById(id);
        if (department.isPresent()) {
            return modelMapper.map(department.get(), DepartmentDto.class);
        } else
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Không tìm thấy cơ sở với id:"+id);
    }

    @Override
    @Transactional
    public DepartmentDto saveDepartment(DepartmentDto departmentDto) {
        Optional<Department> optionalDepartment = departmentRepository.findById(departmentDto.getId());
        Department department;
        if (optionalDepartment.isPresent()) {
            department = optionalDepartment.get();
            if (checkDepartmentHasChange(department, departmentDto)) {
                throw new CustomErrorException(HttpStatus.NOT_ACCEPTABLE, "Mã cơ sở hoặc tên cơ sở đã tồn tại trong hệ thống.");
            }
        } else {
            department = new Department();
            if (checkValidSaveDepartment(departmentDto))
                throw new CustomErrorException(HttpStatus.NOT_ACCEPTABLE, "Mã cơ sở hoặc tên cơ sở đã tồn tại trong hệ thống.");
            department.setStatus(true);
        }
        department.setCode(departmentDto.getCode().toUpperCase());
        department.setName(departmentDto.getName());
        Department savedDepartment = departmentRepository.save(department);
        return modelMapper.map(savedDepartment, DepartmentDto.class);
    }

    private boolean checkDepartmentHasChange(Department oldD, DepartmentDto newD) {
        if ( !oldD.getCode().equals(newD.getCode()) && !oldD.getName().equals(newD.getName()) ) {
            return departmentRepository.existsByCodeOrNameAndStatusTrue(newD.getCode(), newD.getName());
        }
        else if ( !oldD.getCode().equals(newD.getCode()) ) {
            return departmentRepository.existsByCodeAndStatusTrue(newD.getCode());
        } else if ( !oldD.getName().equals(newD.getName()) ) {
            return departmentRepository.existsByNameAndStatusTrue(newD.getName());
        }
        return false;
    }

    private boolean checkValidSaveDepartment(DepartmentDto departmentDto) {
        return departmentRepository.existsByCodeOrNameAndStatusTrue(departmentDto.getCode(), departmentDto.getName());
    }

    @Override
    @Transactional
    public void deleteDepartment(int id) {
        Optional<Department> department = departmentRepository.findById(id);
        if (department.isPresent()) {
            Department deleteD = department.get();
            if (!checkValidDeleteDepartment(deleteD.getId())) {
                deleteD.setStatus(false);
                deleteD.getRooms().forEach(r->r.setStatus(false));
                departmentRepository.save(deleteD);
            } else
                throw new CustomErrorException(HttpStatus.NOT_ACCEPTABLE,
                        "Không thể xóa cơ sở nảy do có ít nhất môt sự kiện đã được đăng ký tổ chức sắp tới");
        } else
            throw new CustomErrorException(HttpStatus.NOT_FOUND, "Không tìm thấy cơ sở với id:"+id);
    }

    private boolean checkValidDeleteDepartment(int departmentId) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        return departmentRepository.existsByIdAndRooms_Events_DateGreaterThanOrRooms_Events_DateEqualsAndRooms_Events_StartTimeGreaterThanEqual(
                departmentId, currentDate, currentDate, currentTime);
    }
}
