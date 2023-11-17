package com.sgu.schedulerApp.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
public class AdminHomeController {
    @GetMapping(value = "/")
    public String home() {
        return "adminpage/index";
    }

    @GetMapping(value = "/room")
    public String room() {
        return "adminpage/room";
    }

    @GetMapping(value = "/classfaculty")
    public String classfaculty() {
        return "adminpage/classfaculty";
    }
}
