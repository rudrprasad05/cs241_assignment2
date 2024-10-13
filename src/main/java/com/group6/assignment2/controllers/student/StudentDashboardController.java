package com.group6.assignment2.controllers.student;

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
public class StudentDashboardController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

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
    public String studentSubjectDetails(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("subject_code") String subject_code, Model model) {
        addLinks(sideNavLinks);

        Student student = studentRepository.findByUsername(userDetails.getUsername());
        Subject subject = subjectRepository.findByCode(subject_code);
        List<SubjectClass> subjectClasses = subject.getSubjectClasses();
        List<Enrollment> enrollments = new ArrayList<>();

        Map<SubjectClass, Enrollment> classEnrollmentMap = new HashMap<>();


//        for(SubjectClass subjectClass : subjectClasses) {
//            enrollments.add(enrollmentRepository.findByStudentIdAndSubjectClassId(student.getId(), subjectClass.getId()));
//        }
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
        return "student/subject-details";
    }

    private static void addLinks(List<Link> links) {
        links.clear();
        links.add(new Link("/student/dashboard", "Dashboard"));
        links.add(new Link("/student/subjects", "Subjects"));
    }

    public List<Enrollment> getFilteredEnrollments(Student s, SubjectClass subjectClassToCheck) {
        // Filter the enrollments where the subject class matches the one you're checking
        List<Enrollment> filteredEnrollments = s.getEnrollments().stream()
                .filter(enrollment -> enrollment.getSubjectClass().equals(subjectClassToCheck))
                .collect(Collectors.toList());

        return filteredEnrollments;
    }
}
