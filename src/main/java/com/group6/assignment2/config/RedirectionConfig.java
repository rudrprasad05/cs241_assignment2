package com.group6.assignment2.config;

import com.group6.assignment2.entity.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RedirectionConfig {
    public static String RedirectToDashboard(User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(user == null) {
            return "redirect:/auth/login";
        }
        if (user instanceof Student) {
            return "redirect:/student/dashboard";
        } else if (user instanceof Parent) {
            return "redirect:/parent/dashboard";
        } else if (user instanceof Teacher) {
            return "redirect:/teacher/dashboard";
        } else if (user instanceof Admin) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/";
        }

    }

    public static String RedirectToNotificationSend(User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(user == null) {
            return "redirect:/auth/login";
        }
        if (user instanceof Student) {
            return "redirect:/student/notifications";
        } else if (user instanceof Parent) {
            return "redirect:/parent/notifications";
        } else if (user instanceof Teacher) {
            return "redirect:/teacher/notifications/send";
        } else if (user instanceof Admin) {
            return "redirect:/admin/notifications/send";
        } else {
            return "redirect:/";
        }

    }
}
