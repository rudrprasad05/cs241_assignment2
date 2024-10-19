package com.group6.assignment2.controllers;

import com.group6.assignment2.config.Link;
import com.group6.assignment2.config.RedirectionConfig;
import com.group6.assignment2.entity.User;
import com.group6.assignment2.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    static List<Link> sideNavLinks = new ArrayList<>();

    public ProfileController() {
        sideNavLinks = Link.addLinks("admin");
    }

}
