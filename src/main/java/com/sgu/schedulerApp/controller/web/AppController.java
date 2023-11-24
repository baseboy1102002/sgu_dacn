package com.sgu.schedulerApp.controller.web;

import com.sgu.schedulerApp.dto.UserDto;
import com.sgu.schedulerApp.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AppController {

    private final RoleService roleService;

    @GetMapping(value = "/")
    public String getHomePage() {
        return "redirect:/event";
    }

    @GetMapping(value = "/login")
    public String getLoginPage() {
        return "webpage/login";
    }

    @GetMapping(value = "/sign-up")
    public String getSignInPage(Model model) {
        model.addAttribute("roles", roleService.findAllRoleExceptAdmin());
        model.addAttribute("userDto", new UserDto());
        return "webpage/sign-up";
    }
}
