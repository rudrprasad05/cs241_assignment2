package com.group6.assignment2.controllers;

import com.group6.assignment2.config.RedirectionConfig;
import com.group6.assignment2.entity.User;
import com.group6.assignment2.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepository;

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

    @GetMapping("/redirect/profile/edit")
    public String redirectToProfile(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) throws IOException {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(null);

        return RedirectionConfig.RedirectToEditProfile(user, null, null);
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

}

