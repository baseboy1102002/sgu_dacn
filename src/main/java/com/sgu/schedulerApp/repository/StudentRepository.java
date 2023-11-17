package com.sgu.schedulerApp.repository;

import com.sgu.schedulerApp.entity.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<StudentInfo, Integer> {
    Boolean existsByStudentCode(String studentCode);
}
