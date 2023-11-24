package com.sgu.schedulerApp.service;

import com.sgu.schedulerApp.dto.RoleDto;

import java.util.List;

public interface RoleService {

    List<RoleDto> findAllRoleExceptAdmin();
}
