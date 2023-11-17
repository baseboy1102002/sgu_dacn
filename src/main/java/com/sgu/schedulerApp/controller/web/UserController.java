package com.sgu.schedulerApp.controller.web;

import com.sgu.schedulerApp.dto.UserDto;
import com.sgu.schedulerApp.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping(value = "/info")
    public String getUserInfo(Model model) {
        model.addAttribute("user", userService.findUserInfo());
        return "webpage/user-info";
    }

    @GetMapping(value = "/update-info")
    public String getUserUpdate(Model model) {
        model.addAttribute("userDto", userService.findUserInfo());
        return "webpage/user-update";
    }

    @GetMapping(value = "/change-password")
    public String getChangePassword() {
        return "webpage/change-password";
    }

    @PostMapping(value = "/update-info")
    public String updateUserInfo(Model model, @ModelAttribute("UserDto") UserDto userDto) {
        UserDto newUser = userService.updateUser(userDto);
        if (newUser != null) {
            model.addAttribute("message", "Cập nhật thông tin cho tài khoản người dùng thành công!");
            model.addAttribute("alert", "success");
            model.addAttribute("user", newUser);
            return "webpage/user-info";
        } else {
            model.addAttribute("alert" , "danger");
            model.addAttribute("userDto", userDto);
            return "webpage/user-update";
        }
    }

    @PostMapping(value = "/change-password")
    public String changePassword(@RequestParam("old_password") String oldPassword,
                                 @RequestParam("new_password") String newPassword,
                                 RedirectAttributes redirectAttributes) {
        Boolean isChangePassSuccess = userService.changePassword(oldPassword, newPassword);
        if (isChangePassSuccess) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật mật khẩu mới thành công!");
            redirectAttributes.addFlashAttribute("alert", "success");
            return "redirect:/user/info";
        } else {
            redirectAttributes.addFlashAttribute("alert", "danger");
            return "redirect:/user/change-password";
        }
    }
}
