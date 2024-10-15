package com.group6.assignment2.controllers.student;

import com.group6.assignment2.config.Link;
import com.group6.assignment2.entity.*;
import com.group6.assignment2.repository.*;
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
    @Autowired
    private SubjectClassRepository subjectClassRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;

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

        sideNavLinks = Link.addLinks("student");

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("pageTitle", "User Dashboard");

        return "student/dashboard";

    }


    @GetMapping("/student/notifications")
    public String studentNotification(Model model, @AuthenticationPrincipal UserDetails userDetails){
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Notification> receivedNotifications = user.getReceivedNotifications();

        model.addAttribute("receivedNotifications", receivedNotifications);
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("notificationTypes", Arrays.asList(Notification.NotificationType.values()));

        return "/notifications";  // Refers to src/main/resources/templates/user/dashboard.html
    }


    @GetMapping("/student/subjects")
    public String studentSubjects(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Student student = studentRepository.findByUsername(userDetails.getUsername());

        List<Subject> subjects = subjectRepository.findAll();
        List<Subject> enrolledSubjects = student.getEnrollments().stream()
                .map(Enrollment::getSubject)
                .toList();

        sideNavLinks = Link.addLinks("student");

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("subjects", subjects);
        model.addAttribute("enrolledSubjects", enrolledSubjects);
        model.addAttribute("pageTitle", "Student Dashboard");
        // Get the authenticated user
        return "student/subjects";
    }

    @GetMapping("/student/subjects/{subject_code}")
    public String studentSubjectDetails(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("subject_code") String subject_code, Model model) {
        sideNavLinks = Link.addLinks("student");

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
        return "student/subject-details";
    }

    @GetMapping("/student/subjects/{subject_code}/{class_code}")
    public String studentClassDetails(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("subject_code") String subject_code, @PathVariable("class_code") String class_code, Model model) {
        Long id = Long.parseLong(class_code);
        Student student = studentRepository.findByUsername(userDetails.getUsername());
        Subject subject = subjectRepository.findByCode(subject_code);
        SubjectClass subjectClass = subjectClassRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Class not found"));
        List<Session> classSessions = subjectClass.getSessions();

        classSessions.sort(Comparator.comparing(Session::getWeek));

        Map<Session, Attendance> classSessionMap = new LinkedHashMap<>();

        for (Session session : classSessions) {
            System.out.println(session.getWeek());
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
