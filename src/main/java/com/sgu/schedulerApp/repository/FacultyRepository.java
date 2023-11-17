package com.sgu.schedulerApp.repository;

import com.sgu.schedulerApp.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
    List<Faculty> findByStatusTrue();

    Faculty findByCode(String code);
}
