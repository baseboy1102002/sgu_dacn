package com.sgu.schedulerApp.config.modelmapper;

import com.sgu.schedulerApp.config.security.MyUserDetails;
import com.sgu.schedulerApp.dto.EventDto;
import com.sgu.schedulerApp.dto.StudentInfoDto;
import com.sgu.schedulerApp.dto.UserDto;
import com.sgu.schedulerApp.entity.*;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {

    Converter<List<EventStudent>, Boolean> toAttendStatus = new
            AbstractConverter<List<EventStudent>, Boolean>() {
                @Override
                protected Boolean convert(List<EventStudent> eventStudents) {
                    int userId;
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    Object principal = authentication.getPrincipal();
                    if (principal instanceof MyUserDetails) {
                        Set<String> currentUserRole = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
                        if (currentUserRole.contains("STUDENT"))
                            userId = ((MyUserDetails) principal).getUser().getStudentInfo().getId();
                        else userId = 0;
                    }
                    else userId = 0;
                    return eventStudents.stream().anyMatch(i -> i.getStudentInfo().getId() == userId);
                }
            };

    Converter<User, Boolean> toCreateStatus = new
            AbstractConverter<User, Boolean>() {
                @Override
                protected Boolean convert(User user) {
                    int userId;
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    Object principal = authentication.getPrincipal();
                    if (principal instanceof MyUserDetails) {
                        Set<String> currentUserRole = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
                        if (currentUserRole.contains("TEACHER"))
                            userId = ((MyUserDetails) principal).getUser().getId();
                        else userId = 0;
                    } else userId = 0;
                    return user.getEvents().stream().anyMatch(i -> i.getUser().getId() == userId);
                }
            };

    Converter<Room, String> toDepartmentName = new
            AbstractConverter<Room, String>() {
                @Override
                protected String convert(Room room) {
                    return room.getDepartment().getName();
                }
            };

    Converter<Room, String> toDepartmentCode = new
            AbstractConverter<Room, String>() {
                @Override
                protected String convert(Room room) {
                    return room.getDepartment().getCode();
                }
            };

    Converter<Event, Boolean> toIsOutDated = new
            AbstractConverter<Event, Boolean>() {
                @Override
                protected Boolean convert(Event event) {
                    LocalDateTime time = LocalDateTime.of(event.getDate(), event.getStartTime());
                    return time.isBefore(LocalDateTime.now());
                }
            };

    PropertyMap<Event, EventDto> eventMap = new PropertyMap<Event, EventDto>()
    {
        protected void configure()
        {
            using(toAttendStatus).map(source.getStudents()).setAttendstatus(false);
            using(toCreateStatus).map(source.getUser()).setCreateStatus(false);
            using(toDepartmentName).map(source.getRoom()).setDepartmentName("");
            using(toDepartmentCode).map(source.getRoom()).setDepartmentCode("");
            using(toIsOutDated).map(source).setIsOutDated(false);
        }
    };

    Converter<User, String> toUserCode = new
            AbstractConverter<User, String>() {
                @Override
                protected String convert(User user) {
                    String userCode = "";
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    Object principal = authentication.getPrincipal();
                    if (principal instanceof MyUserDetails) {
                        Set<String> currentUserRole = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
                        if (currentUserRole.contains("STUDENT"))
                            return user.getStudentInfo().getStudentCode();
                        else if (currentUserRole.contains("TEACHER")) {
                            return user.getTeacherInfo().getTeacherCode();
                        }
                    }
                    return userCode;
                }
            };

    Converter<StudentInfo, String> toStudentEmail = new
            AbstractConverter<StudentInfo, String>() {
                @Override
                protected String convert(StudentInfo studentInfo) {
                    String email = "";
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    Object principal = authentication.getPrincipal();
                    if (principal instanceof MyUserDetails) {
                        Set<String> currentUserRole = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
                        if (currentUserRole.contains("STUDENT"))
                            return studentInfo.getEmail();
                    }
                    return email;
                }
            };

    PropertyMap<User, UserDto> userMap = new PropertyMap<User, UserDto>()
    {
        protected void configure()
        {
            using(toUserCode).map(source).setUserCode("");
            using(toStudentEmail).map(source.getStudentInfo()).setEmail("");
        }
    };

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        modelMapper.addMappings(eventMap);
        modelMapper.addMappings(userMap);
        return  modelMapper;
    }
}
