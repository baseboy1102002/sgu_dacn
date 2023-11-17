package com.sgu.schedulerApp.repository;

import com.sgu.schedulerApp.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    List<Department> findByStatusTrue();
}
