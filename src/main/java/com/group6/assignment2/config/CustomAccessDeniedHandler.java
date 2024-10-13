package com.group6.assignment2.config;

import com.group6.assignment2.entity.Role;
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
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            // Check if the user has any authorities/roles
            if (!auth.getAuthorities().isEmpty()) {
                String role = auth.getAuthorities().iterator().next().getAuthority();
                String redirectUrl = "/default";  // Default redirect if no match

                // Redirect to respective dashboards based on role
                switch (role) {
                    case "ADMIN":  // Admin role
                        redirectUrl = "/admin/dashboard";
                        break;
                    case "TEACHER":  // Teacher role
                        redirectUrl = "/teacher/dashboard";
                        break;
                    case "PARENT":  // Parent role
                        redirectUrl = "/parent/dashboard";
                        break;
                    case "STUDENT":  // User role
                        redirectUrl = "/student/dashboard";
                        break;
                    default:
                        // If no known role, redirect to a default page or error page
                        redirectUrl = "/";
                        break;
                }

                response.sendRedirect(request.getContextPath() + redirectUrl); // Redirect to the role-based dashboard
            } else {
                // Handle case where there are no authorities assigned to the user
                response.sendRedirect(request.getContextPath() + "/access-denied");
            }
        } else {
            // If user is not authenticated, send them to the login page
            response.sendRedirect(request.getContextPath() + "/auth/login");
        }
    }

}
