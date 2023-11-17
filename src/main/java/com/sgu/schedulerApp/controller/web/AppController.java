package com.sgu.schedulerApp.controller.web;

import com.sgu.schedulerApp.config.security.MyUserDetails;
import com.sgu.schedulerApp.service.impl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AppController {

    private final EventServiceImpl eventService;
    private final FacultyServiceImpl facultyService;
    private final ClassRoomServiceImpl classRoomService;
    private final DepartmentServiceImpl departmentService;
    private final RoomServiceImpl roomService;

    @GetMapping(value = "/")
    public String home() {
        return "redirect:/event";
    }

    @GetMapping(value = "/my-event")
    public String myEvent() {
        return "webpage/myschedule";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "webpage/login";
    }
}
