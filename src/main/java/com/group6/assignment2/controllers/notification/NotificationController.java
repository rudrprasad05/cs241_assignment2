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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Controller
public class NotificationController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    static List<Link> sideNavLinks = new ArrayList<>();


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

    private void pushData(Model model, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Notification> receivedNotifications = user.getReceivedNotifications();

        addLinks();
        model.addAttribute("receivedNotifications", receivedNotifications);
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("notificationTypes", Arrays.asList(Notification.NotificationType.values()));
    }


    private static void addLinks() {
        sideNavLinks.clear();
        sideNavLinks.add(new Link("/teacher/dashboard", "Subjects"));
        sideNavLinks.add(new Link("/teacher/notifications", "Notifications"));
        sideNavLinks.add(new Link("/teacher/invite-teacher", "Invite Teacher"));
    }
}
