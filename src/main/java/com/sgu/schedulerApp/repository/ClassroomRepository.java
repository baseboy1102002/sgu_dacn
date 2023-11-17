package com.sgu.schedulerApp.repository;

import com.sgu.schedulerApp.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
    List<Classroom> findByStatusTrue();

    Classroom findByCode(String code);
}
