package com.sgu.schedulerApp.service.impl;

import com.sgu.schedulerApp.dto.RoleDto;
import com.sgu.schedulerApp.entity.Role;
import com.sgu.schedulerApp.repository.RoleRepository;
import com.sgu.schedulerApp.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<RoleDto> findAllRoleExceptAdmin() {
        List<Role> roles = roleRepository.findByRoleCodeNot("ADMIN");
        return roles.stream().map(r -> modelMapper.map(r, RoleDto.class)).collect(Collectors.toList());
    }
}
