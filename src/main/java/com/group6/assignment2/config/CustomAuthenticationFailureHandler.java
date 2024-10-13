package com.group6.assignment2.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Invalid username or password";
        System.out.println("Login failed: " + exception.getMessage());
        if (exception.getMessage().equalsIgnoreCase("User not found")) {
            errorMessage = "Email address not in use";
        } else if (exception.getMessage().equalsIgnoreCase("Bad credentials")) {
            errorMessage = "Invalid username or password";
        }

        request.getSession().setAttribute("toastMessage", "Login failed! Please check your credentials.");
        request.getSession().setAttribute("toastTitle", "Error");
        request.getSession().setAttribute("toastClass", "bg-danger");

        // Redirect to the login page with the error message as a query parameter
        response.sendRedirect("/auth/login?error=" + errorMessage);
    }

}
