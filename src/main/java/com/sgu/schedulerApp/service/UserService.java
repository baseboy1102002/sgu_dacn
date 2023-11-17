package com.sgu.schedulerApp.service;

import com.sgu.schedulerApp.dto.UserDto;
import com.sgu.schedulerApp.entity.User;

public interface UserService {

    UserDto findUserInfo();

    UserDto updateUser(UserDto userDto);

    Boolean isUserCodeExists(String userCode);

    Boolean changePassword(String oldPassword, String newPassword);
}
