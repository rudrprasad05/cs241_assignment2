package com.group6.assignment2.controllers.notification;

import com.group6.assignment2.config.Link;
import com.group6.assignment2.entity.*;
import com.group6.assignment2.repository.NotificationRepository;
import com.group6.assignment2.repository.StudentRepository;
import com.group6.assignment2.repository.SubjectRepository;
import com.group6.assignment2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class GetNotificationController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    static List<Link> sideNavLinks = new ArrayList<>();

    public GetNotificationController() {
        sideNavLinks = Link.addLinks("admin");
    }

    @GetMapping("/teacher/notifications")
    public String teacherNotification(Model model, @AuthenticationPrincipal UserDetails userDetails){
        pushData(model, userDetails);
        return "/notifications";  // Refers to src/main/resources/templates/user/dashboard.html
    }

    @GetMapping("/student/notifications")
    public String studentNotification(Model model, @AuthenticationPrincipal UserDetails userDetails){
        pushData(model, userDetails);
        return "/notifications";  // Refers to src/main/resources/templates/user/dashboard.html
    }

    @GetMapping("/admin/notifications")
    public String adminNotification(Model model, @AuthenticationPrincipal UserDetails userDetails){
        pushData(model, userDetails);
        return "/notifications";  // Refers to src/main/resources/templates/user/dashboard.html
    }

    @GetMapping("/parent/notifications")
    public String parentNotification(Model model, @AuthenticationPrincipal UserDetails userDetails){
        pushData(model, userDetails);
        return "/notifications";  // Refers to src/main/resources/templates/user/dashboard.html
    }


    @GetMapping("/teacher/notifications/send")
    public String teacherSendNotification(Model model, @AuthenticationPrincipal UserDetails userDetails){

        Teacher teacher = (Teacher) userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Notification> receivedNotifications = teacher.getReceivedNotifications();

        List<User> receivers = getEligibleReceivers(teacher);

        model.addAttribute("receivers", receivers);
        model.addAttribute("receivedNotifications", receivedNotifications);
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("notificationTypes", Arrays.asList(Notification.NotificationType.values()));


        return "teacher/sendNotifications";  // Refers to src/main/resources/templates/user/dashboard.html
    }

    @GetMapping("/admin/notifications/send")
    public String adminSendNotification(Model model, @AuthenticationPrincipal UserDetails userDetails){

        Admin admin = (Admin) userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Notification> receivedNotifications = admin.getReceivedNotifications();

        List<User> receivers = getEligibleReceivers(admin);

        model.addAttribute("receivers", receivers);
        model.addAttribute("receivedNotifications", receivedNotifications);
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("notificationTypes", Arrays.asList(Notification.NotificationType.values()));

        return "admin/sendNotifications";  // Refers to src/main/resources/templates/user/dashboard.html
    }

    // Helper method to get eligible receivers (students and admins)
    private List<User> getEligibleReceivers(User user) {
        // Fetch students and admins associated with subjects the teacher is teaching
        List<Subject> subjects = subjectRepository.findByTeacher(user.getId());

        List<User> eligibleReceivers = new ArrayList<>();
        List<Enrollment> enrollments = new ArrayList<>();

        for (Subject subject : subjects) {
            enrollments.addAll(subject.getEnrollments()); // Assuming subjects track enrolled students
        }

        for (Enrollment enrollment : enrollments) {
            eligibleReceivers.add(enrollment.getStudent());
        }

        // Also fetch all admins (if needed)
        List<User> admins = userRepository.findByRole(Role.ADMIN);
        eligibleReceivers.addAll(admins);

        return eligibleReceivers;
    }

    private void pushData(Model model, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Notification> receivedNotifications = user.getReceivedNotifications();

        model.addAttribute("receivedNotifications", receivedNotifications);
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("notificationTypes", Arrays.asList(Notification.NotificationType.values()));
    }

}
