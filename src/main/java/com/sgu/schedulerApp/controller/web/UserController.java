package com.sgu.schedulerApp.controller.web;

import com.sgu.schedulerApp.dto.UserDto;
import com.sgu.schedulerApp.entity.User;
import com.sgu.schedulerApp.exception.CustomErrorException;
import com.sgu.schedulerApp.service.EmailService;
import com.sgu.schedulerApp.service.RoleService;
import com.sgu.schedulerApp.service.UserService;
import com.sgu.schedulerApp.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final EmailService emailService;

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

    @GetMapping(value = "/reset-password")
    public String getResetPassword(@RequestParam String token, Model model) {
        try {
            User user = userService.findByUserToken(token);
        } catch (CustomErrorException e) {
            model.addAttribute("alert", "danger");
            model.addAttribute("message", e.getMessage());
        }
        model.addAttribute("token", token);
        return "webpage/reset-password";
    }

    @PostMapping(value = "/update-info")
    public String updateUserInfo(Model model, @ModelAttribute("UserDto") UserDto userDto) {
        try {
            UserDto newUser = userService.updateUser(userDto);
            model.addAttribute("message", "Cập nhật thông tin cho tài khoản người dùng thành công!");
            model.addAttribute("alert", "success");
            model.addAttribute("user", newUser);
            return "webpage/user-info";
        } catch (CustomErrorException e) {
            model.addAttribute("alert" , "error");
            model.addAttribute("message", e.getMessage());
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

    @PostMapping(value = "/sign-up")
    public String registerUserAccount(Model model, @ModelAttribute("userDto") UserDto userDto) {
        try {
            userService.saveUser(userDto);
            model.addAttribute("alert", "success");
            model.addAttribute("message", "Đăng ký tài khoản thành công!");
            return "webpage/login";
        } catch (CustomErrorException e) {
            model.addAttribute("alert", "danger");
            model.addAttribute("message", e.getMessage());
            model.addAttribute("userDto", userDto);
            model.addAttribute("roles", roleService.findAllRoleExceptAdmin());
            return "webpage/sign-up";
        }
    }

    @PostMapping(value = "/forgot-password")
    public String forgotPassord(@RequestParam String email, HttpServletRequest request, Model model) {
        String token = RandomString.make(25);
        try {
            userService.setUserTokenByEmail(token, email);
            String resetLink = (request.getRequestURL().toString()).replace(request.getServletPath(), "")+"/reset-password?token="+token;
            emailService.sendResetPasswordEmail(email, resetLink);
            model.addAttribute("alert", "success");
            model.addAttribute("message", "Gửi thành công yêu cầu reset mật khẩu tới email của bạn. Vui lòng kiểm tra");
        } catch (CustomErrorException e) {
            model.addAttribute("alert", "danger");
            model.addAttribute("message", e.getMessage());
        }
        return "webpage/sign-up";
    }

    @PostMapping(value = "/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String password, Model model) {
        try {
            User user = userService.findByUserToken(token);
            userService.resetPassword(user, password);
            model.addAttribute("alert", "success");
            model.addAttribute("message", "Reset mật khẩu mới thành công!");
            return "webpage/login";
        } catch (CustomErrorException e) {
            model.addAttribute("alert", "danger");
            model.addAttribute("message", e.getMessage());
            return "webpage/reset-password";
        }
    }
}
