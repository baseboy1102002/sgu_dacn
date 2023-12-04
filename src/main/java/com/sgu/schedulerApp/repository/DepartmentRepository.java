package com.sgu.schedulerApp.repository;

import com.sgu.schedulerApp.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    List<Department> findByStatusTrue();

    Department findByCodeAndStatusTrue(String departmentCode);

    @Query("select d from Department d where (:departmentCode is null or d.code=:departmentCode) and d.status = true")
    List<Department> findByCodeOptionalAndStatusTrue(String departmentCode);

    boolean existsByCodeOrNameAndStatusTrue(String departmentCode, String departmentName); // when create new or // when update case3: both name & code changes

    boolean existsByCodeAndStatusTrue(String departmentCode);   // when update case1: only name changes

    boolean existsByNameAndStatusTrue(String departmentName);    // when update case2: only code changes

    boolean existsByIdAndRooms_Events_DateGreaterThanOrRooms_Events_DateEqualsAndRooms_Events_StartTimeGreaterThanEqual(int departmentId, LocalDate currentDay1, LocalDate currentDay2, LocalTime currentTime);   // when delete department

}
