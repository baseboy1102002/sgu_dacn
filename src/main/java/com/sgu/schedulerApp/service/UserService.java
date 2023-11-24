package com.sgu.schedulerApp.service;

import com.sgu.schedulerApp.dto.UserDto;
import com.sgu.schedulerApp.entity.User;

public interface UserService {

    UserDto findUserInfo();

    UserDto updateUser(UserDto userDto);

    Boolean changePassword(String oldPassword, String newPassword);

    void saveUser(UserDto userDto);
}
