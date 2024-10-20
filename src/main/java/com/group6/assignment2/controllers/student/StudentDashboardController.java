package com.group6.assignment2.controllers.student;

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
    @Autowired
    private SubjectClassRepository subjectClassRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;

    public StudentDashboardController() {
        sideNavLinks = Link.addLinks("student");

    }

    @GetMapping("/student")
    public String redirectToDashboard(Model model) {
        return "redirect:/student/dashboard";
    }

    @GetMapping("/student/dashboard")
    public String userDashboard(Model model) {
        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Student student = studentRepository.findByUsername(username).orElseThrow(null);

        if(student == null) {
            return "redirect:/student/dashboard";
        }

        List<Subject> enrolledSubjects = student.getEnrollments().stream()
                .filter(e -> e.isAccepted() == Enrollment.EnrollmentStatus.ACCEPTED)
                .map(Enrollment::getSubject)
                .toList();

        Set<Subject> uniqueSet = new HashSet<>(enrolledSubjects); // HashSet removes duplicates
        List<Subject> uniqueList = new ArrayList<>(uniqueSet);

        sideNavLinks = Link.addLinks("student");

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("enrolledSubjects", uniqueList);
        model.addAttribute("pageTitle", "User Dashboard");

        return "student/dashboard";

    }


    @GetMapping("/student/notifications")
    public String studentNotification(Model model, @AuthenticationPrincipal UserDetails userDetails){
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Notification> receivedNotifications = user.getReceivedNotifications();

        model.addAttribute("receivedNotifications", receivedNotifications);
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("notificationTypes", Arrays.asList(Notification.NotificationType.values()));

        return "/notifications";  // Refers to src/main/resources/templates/user/dashboard.html
    }


    @GetMapping("/student/subjects")
    public String studentSubjects(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Student student = studentRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Subject> subjects = subjectRepository.findAll();
        List<Subject> enrolledSubjects = student.getEnrollments().stream()
                .filter(e -> e.isAccepted() == Enrollment.EnrollmentStatus.ACCEPTED)
                .map(Enrollment::getSubject)
                .toList();

        Set<Subject> uniqueSet = new HashSet<>(enrolledSubjects); // HashSet removes duplicates
        List<Subject> uniqueList = new ArrayList<>(uniqueSet);

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("subjects", subjects);
        model.addAttribute("enrolledSubjects", uniqueList);
        model.addAttribute("pageTitle", "Student Dashboard");
        // Get the authenticated user
        return "student/subjects";
    }

    @GetMapping("/student/subjects/{subject_code}")
    public String studentSubjectDetails(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("subject_code") String subject_code, Model model) {

        Student student = studentRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Subject subject = subjectRepository.findByCode(subject_code);
        List<SubjectClass> subjectClasses = subjectClassRepository.findBySubjectCode(subject_code);
        List<Enrollment> enrollments = new ArrayList<>();

        Map<SubjectClass, Enrollment> classEnrollmentMap = new HashMap<>();
        subjectClasses.sort(Comparator.comparing(SubjectClass::getId));

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

    @GetMapping("/student/profile")
    public String studentProfile(
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


    @GetMapping("/student/profile/edit")
    public String studentProfileEdit(
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

    @GetMapping("/student/subjects/{subject_code}/{class_code}")
    public String studentClassDetails(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("subject_code") String subject_code, @PathVariable("class_code") String class_code, Model model) {
        Long id = Long.parseLong(class_code);
        Student student = studentRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Subject subject = subjectRepository.findByCode(subject_code);
        SubjectClass subjectClass = subjectClassRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Class not found"));
        List<Session> classSessions = subjectClass.getSessions();

        classSessions.sort(Comparator.comparing(Session::getWeek));

        Map<Session, Attendance> classSessionMap = new LinkedHashMap<>();

        for (Session session : classSessions) {
            Attendance attendance = attendanceRepository.findByStudentIdAndSessionId(student.getId(), session.getId());
            classSessionMap.put(session, attendance);  // Add to the map, enrollment could be null if not enrolled
        }

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("subject", subject);
        model.addAttribute("subjectClass", subjectClass);
        model.addAttribute("classSessions", classSessions);
        model.addAttribute("classSessionMap", classSessionMap);
        model.addAttribute("student", student);
        model.addAttribute("pageTitle", "Student Dashboard");
        // Get the authenticated user
        return "student/class-details";
    }
}
