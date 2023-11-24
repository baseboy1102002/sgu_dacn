package com.sgu.schedulerApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private int id;
    private String username;
    private String password;
    private String roleName;
    private String roleCode;
    private String fullName;
    private String userCode;
    private String email;
}
