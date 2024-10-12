package com.group6.assignment2.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            String role = auth.getAuthorities().iterator().next().getAuthority();
            String redirectUrl = "/default";

            // Redirect to respective dashboards based on role
            switch (role) {
                case "3":  // Admin
                    redirectUrl = "/admin/dashboard";
                    break;
                case "2":  // Teacher
                    redirectUrl = "/teacher/dashboard";
                    break;
                case "1":  // Parent
                    redirectUrl = "/parent/dashboard";
                    break;
                case "0":  // User
                    redirectUrl = "/user/dashboard";
                    break;
            }

            response.sendRedirect(request.getContextPath() + redirectUrl); // Redirect to the role-based dashboard
        } else {
            // If user is not authenticated, send them to the login page
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

}
