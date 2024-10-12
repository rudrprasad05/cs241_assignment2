package com.group6.assignment2.controllers.student;

import com.group6.assignment2.config.Link;
import com.group6.assignment2.entity.Student;
import com.group6.assignment2.repository.StudentRepository;
import com.group6.assignment2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class StudentDashboardController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/student")
    public String redirectToDashboard(Model model) {
        return "redirect:/student/dashboard";
    }

    @GetMapping("/student/dashboard")
    public String userDashboard(Model model) {
        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Student student = studentRepository.findByEmail(username);

        List<Link> sideNavLinks = new ArrayList<>();

        sideNavLinks.add(new Link("/user/dashboard", "Dashboard"));
        sideNavLinks.add(new Link("/user/subjects", "subjects"));
        sideNavLinks.add(new Link("/user/profile", "Profile"));

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("pageTitle", "User Dashboard");

        return "student/dashboard";

    }

    @GetMapping("/user/subjects")
    public String userSubjects(Model model) {
        // Get the authenticated user
        return "/";
    }
}
