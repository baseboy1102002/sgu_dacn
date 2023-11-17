package com.sgu.schedulerApp.service.impl;

import com.sgu.schedulerApp.config.security.MyUserDetails;
import com.sgu.schedulerApp.dto.UserDto;
import com.sgu.schedulerApp.entity.User;
import com.sgu.schedulerApp.exception.CustomErrorException;
import com.sgu.schedulerApp.repository.UserRepository;
import com.sgu.schedulerApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto findUserInfo() {
        Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = ((MyUserDetails) principal).getUser().getId();
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User u = user.get();
            return modelMapper.map(u, UserDto.class);
        } else throw new CustomErrorException(HttpStatus.NOT_FOUND, "Không tìm thấy user với id: "+userId ,userId);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User oldUser = userRepository.findById(userDto.getId()).get();
        Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> currentUserRole = ((MyUserDetails) principal).getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        if (currentUserRole.contains("STUDENT")) {
            if (!userDto.getUserCode().equals(oldUser.getStudentInfo().getStudentCode())) {
                if (userRepository.existsByStudentInfo_StudentCode(userDto.getUserCode()))
                    return null;
            }
            oldUser.getStudentInfo().setStudentCode(userDto.getUserCode());
            oldUser.getStudentInfo().setEmail(userDto.getEmail());
        } else if (currentUserRole.contains("TEACHER")) {
            if (!userDto.getUserCode().equals(oldUser.getTeacherInfo().getTeacherCode())) {
                if (userRepository.existsByTeacherInfo_TeacherCode(userDto.getUserCode()))
                    return null;
            }
            oldUser.getTeacherInfo().setTeacherCode(userDto.getUserCode());
        }
        oldUser.setFullName(userDto.getFullName());
        User newUser = userRepository.save(oldUser);
        return modelMapper.map(newUser, UserDto.class);
    }

    @Override
    public Boolean isUserCodeExists(String userCode) {
        Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> currentUserRole = ((MyUserDetails) principal).getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        if (currentUserRole.contains("STUDENT")) {
            return userRepository.existsByStudentInfo_StudentCode(userCode);
        } else if (currentUserRole.contains("TEACHER")) {
            return userRepository.existsByTeacherInfo_TeacherCode(userCode);
        } else return false;
    }

    @Override
    public Boolean changePassword(String oldPassword, String newPassword) {
        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(myUserDetails.getUsername());
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        } else return false;
    }
}
