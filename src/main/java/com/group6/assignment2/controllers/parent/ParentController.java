package com.group6.assignment2.controllers.parent;

import com.group6.assignment2.config.Link;
import com.group6.assignment2.entity.*;
import com.group6.assignment2.repository.EnrollmentRepository;
import com.group6.assignment2.repository.StudentRepository;
import com.group6.assignment2.repository.SubjectRepository;
import com.group6.assignment2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.util.stream.Collectors;


@Controller
public class ParentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    List<Link> sideNavLinks = new ArrayList<>();

    @GetMapping("/parent")
    public String redirectToDashboard(Model model) {
        return "redirect:/parent/dashboard";
    }

    @GetMapping("/parent/dashboard")
    public String parent(Model model) {
        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Student student = studentRepository.findByEmail(username);

        sideNavLinks = Link.addLinks("parent");

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("pageTitle", "parent Dashboard");

        return "parent/dashboard";

    }

    @GetMapping("/parent/subjects")
    public String studentSubjects(Model model) {

        List<Subject> subjects = subjectRepository.findAll();

        sideNavLinks = Link.addLinks("parent");

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("subjects", subjects);
        model.addAttribute("pageTitle", "Student Dashboard");
        // Get the authenticated user
        return "parent/subjects";
    }

    @GetMapping("/parent/subjects/{subject_code}")
    public String studentSubjectDetails(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("subject_code") String subject_code, Model model) {
        sideNavLinks = Link.addLinks("parent");

        Student student = studentRepository.findByUsername(userDetails.getUsername());
        Subject subject = subjectRepository.findByCode(subject_code);
        List<SubjectClass> subjectClasses = subject.getSubjectClasses();
        List<Enrollment> enrollments = new ArrayList<>();

        Map<SubjectClass, Enrollment> classEnrollmentMap = new HashMap<>();

        for (SubjectClass subjectClass : subjectClasses) {
            Enrollment enrollment = enrollmentRepository.findByStudentIdAndSubjectClassId(student.getId(), subjectClass.getId());
            classEnrollmentMap.put(subjectClass, enrollment);  // Add to the map, enrollment could be null if not enrolled
        }

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("subject", subject);
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("subjectClasses", subjectClasses);
        model.addAttribute("student", student);
        model.addAttribute("classEnrollmentMap", classEnrollmentMap);
        model.addAttribute("pageTitle", "Student Dashboard");
        // Get the authenticated user
        return "parent/subject-details";
    }

}
