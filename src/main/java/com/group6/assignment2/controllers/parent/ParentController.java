package com.group6.assignment2.controllers.parent;

import com.group6.assignment2.config.Link;
import com.group6.assignment2.entity.*;
import com.group6.assignment2.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.util.*;


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
    @Autowired
    private SubjectClassRepository subjectClassRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private ParentRepository parentRepository;

    public ParentController() {
        sideNavLinks = Link.addLinks("parent");
    }

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

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("pageTitle", "parent Dashboard");

        return "parent/dashboard";

    }
    @GetMapping("/parent/notifications")
    public String parentNotification(Model model, @AuthenticationPrincipal UserDetails userDetails){
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Notification> receivedNotifications = user.getReceivedNotifications();

        model.addAttribute("receivedNotifications", receivedNotifications);
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("notificationTypes", Arrays.asList(Notification.NotificationType.values()));

        return "/notifications";  // Refers to src/main/resources/templates/user/dashboard.html
    }


    @GetMapping("/parent/subjects")
    public String studentSubjects(Model model) {

        List<Subject> subjects = subjectRepository.findAll();

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("subjects", subjects);
        model.addAttribute("pageTitle", "Student Dashboard");
        // Get the authenticated user
        return "parent/subjects";
    }

    @GetMapping("/parent/child")
    public String getChild(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {

        Parent parent = (Parent) userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Student child = studentRepository.findByStudentId(parent.getStudent().getStudentId());
        List<Enrollment> enrollments = child.getEnrollments();
        List<SubjectClass> subjectClasses = new ArrayList<>();
        List<Subject> subjects = new ArrayList<>();

        for (Enrollment enrollment : enrollments) {
            SubjectClass subjectClass = enrollment.getSubjectClass();
            subjectClasses.add(subjectClass);
            subjects.add(subjectClass.getSubject());
        }


        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("child", child);
        model.addAttribute("subjectClasses", subjectClasses);
        model.addAttribute("pageTitle", "Student Dashboard");
        // Get the authenticated user
        return "parent/child";
    }

    @GetMapping("/parent/child/class/{classId}")
    public String studentSubjectDetails(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("classId") Long classId, 
            Model model) {

        Parent parent = parentRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Student student = parent.getStudent();
        SubjectClass subjectClass = subjectClassRepository.findById(classId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Session> sessions = subjectClass.getSessions();

        Map<Session, Attendance> sessionEnrollmentHashMap = new LinkedHashMap<>();

        for(Session session : sessions) {
            Attendance attendance = attendanceRepository.findByStudentIdAndSessionId(student.getId(), session.getId());
            sessionEnrollmentHashMap.put(session, attendance);
        }


        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("attendances", sessionEnrollmentHashMap);
        model.addAttribute("student", student);
        model.addAttribute("classEnrollmentMap", sessionEnrollmentHashMap);
        model.addAttribute("pageTitle", "Student Dashboard");
        // Get the authenticated user
        return "parent/class-details";
    }


    @GetMapping("/parent/profile")
    public String parentProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        model.addAttribute("user", user);
        model.addAttribute("sideNavLinks", sideNavLinks);

        return "/profile";
    }


    @GetMapping("/parent/profile/edit")
    public String parentProfileEdit(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        model.addAttribute("user", user);
        model.addAttribute("sideNavLinks", sideNavLinks);

        return "/edit-profile";
    }


}
