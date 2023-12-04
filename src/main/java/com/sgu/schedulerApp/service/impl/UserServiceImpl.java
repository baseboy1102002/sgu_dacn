package com.sgu.schedulerApp.service.impl;

import com.sgu.schedulerApp.config.security.MyUserDetails;
import com.sgu.schedulerApp.dto.UserDto;
import com.sgu.schedulerApp.entity.Role;
import com.sgu.schedulerApp.entity.StudentInfo;
import com.sgu.schedulerApp.entity.TeacherInfo;
import com.sgu.schedulerApp.entity.User;
import com.sgu.schedulerApp.exception.CustomErrorException;
import com.sgu.schedulerApp.repository.RoleRepository;
import com.sgu.schedulerApp.repository.StudentRepository;
import com.sgu.schedulerApp.repository.TeacherRepository;
import com.sgu.schedulerApp.repository.UserRepository;
import com.sgu.schedulerApp.service.UserService;
import jakarta.transaction.Transactional;
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
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
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
        } else throw new CustomErrorException(HttpStatus.NOT_FOUND, "Không tìm thấy user với id: "+userId);
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
                    throw new CustomErrorException(HttpStatus.NOT_ACCEPTABLE, "Mã sinh viên bị trùng");
                oldUser.getStudentInfo().setStudentCode(userDto.getUserCode());
            }
        } else if (currentUserRole.contains("TEACHER")) {
            if (!userDto.getUserCode().equals(oldUser.getTeacherInfo().getTeacherCode())) {
                if (userRepository.existsByTeacherInfo_TeacherCode(userDto.getUserCode()))
                    throw new CustomErrorException(HttpStatus.NOT_ACCEPTABLE, "Mã giảng viên bị trùng");
                oldUser.getTeacherInfo().setTeacherCode(userDto.getUserCode());
            }
        }
        oldUser.setFullName(userDto.getFullName());
        if (!userDto.getEmail().equals(oldUser.getEmail())) {
            if (userRepository.existsByEmail(userDto.getEmail()))
                throw new CustomErrorException(HttpStatus.NOT_ACCEPTABLE, "Email đã được sử dụng bởi một tài khoản khác");
            oldUser.setEmail(userDto.getEmail());
        }
        User newUser = userRepository.save(oldUser);
        return modelMapper.map(newUser, UserDto.class);
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

    @Override
    @Transactional
    public void saveUser(UserDto userDto) {
        if (checkUserEmailExists(userDto.getEmail()))
            throw new CustomErrorException(HttpStatus.NOT_ACCEPTABLE, "Email này đã được sử dụng");
        if (checkUserCodeExists(userDto.getUserCode(), userDto.getRoleCode()))
            throw new CustomErrorException(HttpStatus.NOT_ACCEPTABLE, userDto.getRoleCode().equals("STUDENT")?
                    "Mã số sinh viên đã được sử dụng": "Mã giảng viên đã được sử dụng");
        User user = modelMapper.map(userDto, User.class);
        Role role = roleRepository.findByRoleCode(userDto.getRoleCode());
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User savedUser = userRepository.save(user);
        if (userDto.getRoleCode().equals("STUDENT")) {
            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setUser(savedUser);
            studentInfo.setStudentCode(userDto.getUserCode());
            studentRepository.save(studentInfo);
        } else if (userDto.getRoleCode().equals("TEACHER")) {
            TeacherInfo teacherInfo = new TeacherInfo();
            teacherInfo.setUser(savedUser);
            teacherInfo.setTeacherCode(userDto.getUserCode());
            teacherRepository.save(teacherInfo);
        }
    }

    private boolean checkUserEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean checkUserCodeExists(String userCode, String userRole) {
        if (userRole.equals("TEACHER")) {
            return teacherRepository.existsByTeacherCode(userCode);
        } else if (userRole.equals("STUDENT")) {
            return studentRepository.existsByStudentCode(userCode);
        }
        return false;
    }

    @Override
    public void setUserTokenByEmail(String token, String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setToken(token);
            userRepository.save(user);
        } else throw new CustomErrorException(HttpStatus.NOT_FOUND, "Không tồn tại tài khoản với email: "+email);
    }

    @Override
    public User findByUserToken(String token) {
        User user = userRepository.findByToken(token);
        if (user != null) {
            return user;
        } else throw new CustomErrorException(HttpStatus.NOT_FOUND, "Mã token không hợp lệ.");
    }

    @Override
    public void resetPassword(User user, String newPassword) {
        String encodedPass = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPass);
        user.setToken(null);
        userRepository.save(user);
    }
}
