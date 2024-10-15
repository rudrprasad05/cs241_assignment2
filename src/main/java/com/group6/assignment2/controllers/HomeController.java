package com.group6.assignment2.controllers;

import com.group6.assignment2.config.RedirectionConfig;
import com.group6.assignment2.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/dashboard")
    public String homeDash(HttpServletRequest request) throws IOException {
        User user = (User) request.getSession().getAttribute("currentUser");

        return RedirectionConfig.RedirectToDashboard(user, null, null);
    }

    @GetMapping("/redirect/notification/send")
    public String redirectToNotificationCenter(HttpServletRequest request) throws IOException {
        User user = (User) request.getSession().getAttribute("currentUser");

        return RedirectionConfig.RedirectToNotificationSend(user, null, null);
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

}

