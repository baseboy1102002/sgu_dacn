package com.sgu.schedulerApp.repository;

import com.sgu.schedulerApp.entity.TeacherInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<TeacherInfo, Integer> {

    Boolean existsByTeacherCode(String teacherCode);
}
