package com.sgu.schedulerApp.repository;

import com.sgu.schedulerApp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    List<Role> findByRoleCodeNot(String admin);

    Role findByRoleCode(String roleCode);
}
