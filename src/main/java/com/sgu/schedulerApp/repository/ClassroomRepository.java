package com.sgu.schedulerApp.repository;

import com.sgu.schedulerApp.entity.Classroom;
import com.sgu.schedulerApp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
    List<Classroom> findByStatusTrue();

    Classroom findByCodeAndStatusTrue(String code);

    @Query(value = "select c from Classroom c where (:classCode is null or c.code like %:classCode%)" +
            " and c.faculty.code=:facultyCode and c.status=true")
    List<Classroom> findByKeywordAndDepartmentCode(String classCode, String facultyCode);

    boolean existsByCodeAndStatusTrue(String classCode);
}
