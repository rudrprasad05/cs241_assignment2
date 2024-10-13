package com.group6.assignment2.controllers.student;

import com.group6.assignment2.config.Link;
import com.group6.assignment2.entity.Student;
import com.group6.assignment2.entity.Subject;
import com.group6.assignment2.entity.SubjectClass;
import com.group6.assignment2.repository.StudentRepository;
import com.group6.assignment2.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;


@Controller
public class StudentDashboardController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    List<Link> sideNavLinks = new ArrayList<>();

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

        addLinks(sideNavLinks);

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("pageTitle", "User Dashboard");

        return "student/dashboard";

    }

    @GetMapping("/student/subjects")
    public String studentSubjects(Model model) {

        List<Subject> subjects = subjectRepository.findAll();

        addLinks(sideNavLinks);

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("subjects", subjects);
        model.addAttribute("pageTitle", "Student Dashboard");
        // Get the authenticated user
        return "student/subjects";
    }

    @GetMapping("/student/subjects/{subject_code}")
    public String studentSubjectDetails(@PathVariable("subject_code") String subject_code, Model model) {

        Subject subject = subjectRepository.findByCode(subject_code);
        List<SubjectClass> subjectClasses = subject.getSubjectClasses();

        addLinks(sideNavLinks);

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("subject", subject);
        model.addAttribute("subjectClasses", subjectClasses);
        model.addAttribute("pageTitle", "Student Dashboard");
        // Get the authenticated user
        return "student/subject-details";
    }

    private static void addLinks(List<Link> links) {
        links.clear();
        links.add(new Link("/student/dashboard", "Dashboard"));
        links.add(new Link("/student/subjects", "Subjects"));
    }
}
