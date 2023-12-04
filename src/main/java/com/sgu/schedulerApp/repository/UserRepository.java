package com.sgu.schedulerApp.repository;

import com.sgu.schedulerApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    Boolean existsByStudentInfo_StudentCode(String studentCode);

    Boolean existsByTeacherInfo_TeacherCode(String teacherCode);

    Boolean existsByEmail(String email);

    User findByEmail(String email);

    User findByToken(String token);

}
