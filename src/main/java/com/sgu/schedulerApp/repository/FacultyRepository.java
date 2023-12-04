package com.sgu.schedulerApp.repository;

import com.sgu.schedulerApp.dto.FacultyDto;
import com.sgu.schedulerApp.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
    List<Faculty> findByStatusTrue();

    Faculty findByCodeAndStatusTrue(String code);

    @Query("select f from Faculty f where (:facultyCode is null or f.code=:facultyCode) and f.status = true")
    List<Faculty> findByCodeOptionalAndStatusTrue(String facultyCode);

    boolean existsByCodeOrNameAndStatusTrue(String facultyCode, String facultyName); // when create new or // when update case3: both name & code changes

    boolean existsByCodeAndStatusTrue(String facultyCode);   // when update case1: only name changes

    boolean existsByNameAndStatusTrue(String facultyName);

    @Query(name = "statistic_top7_faculty_query", nativeQuery = true)
    List<FacultyDto> getStatisticFacultyData(String yearNo, LocalDate currentDate, LocalTime currentTime);
}
