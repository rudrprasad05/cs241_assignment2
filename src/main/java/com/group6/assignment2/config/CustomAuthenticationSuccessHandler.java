package com.group6.assignment2.config;

import com.group6.assignment2.entity.*;
import com.group6.assignment2.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String username = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Store the user in the session
        request.getSession().setAttribute("currentUser", user);

        request.getSession().setAttribute("toastMessage", "Login successful!");
        request.getSession().setAttribute("toastTitle", "Success");
        request.getSession().setAttribute("toastClass", "bg-success");

        switch (user) {
            case Student student -> response.sendRedirect("/student/dashboard");
            case Parent parent -> response.sendRedirect("/parent/dashboard");
            case Teacher teacher -> response.sendRedirect("/teacher/dashboard");
            case Admin admin -> response.sendRedirect("/admin/dashboard");
            case null, default -> response.sendRedirect("/");
        }
    }
}

