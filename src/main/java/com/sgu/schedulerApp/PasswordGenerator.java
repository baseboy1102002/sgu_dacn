package com.sgu.schedulerApp;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "student";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encoder.matches("student", encodedPassword));
    }
}
