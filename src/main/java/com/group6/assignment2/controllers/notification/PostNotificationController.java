package com.group6.assignment2.controllers.notification;

import com.group6.assignment2.config.Link;
import com.group6.assignment2.entity.*;
import com.group6.assignment2.repository.NotificationRepository;
import com.group6.assignment2.repository.StudentRepository;
import com.group6.assignment2.repository.SubjectRepository;
import com.group6.assignment2.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class PostNotificationController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    static List<Link> sideNavLinks = new ArrayList<>();

    @PostMapping("/notification/seen")
    public String seenNotification(
            @RequestParam("id") Long id,
            HttpServletRequest request

    ) {
        // Get sender (teacher) and receiver
       Notification notification = notificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Receiver not found"));
       notification.setSeen(true);

        // Save the notification
        notificationRepository.save(notification);

        String referer = request.getHeader("Referer");
        System.out.println(referer);
        return "redirect:" + referer;
    }

    @PostMapping("/notification/delete")
    public String deleteNotification(
            @RequestParam("id") Long id,
            HttpServletRequest request

    ) {
        // Get sender (teacher) and receiver
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new RuntimeException("Receiver not found"));

        notificationRepository.delete(notification);

        String referer = request.getHeader("Referer");
        System.out.println(referer);
        return "redirect:" + referer;
    }

    @PostMapping("/teacher/send-notification")
    public String sendNotification(
            @RequestParam("title") String title,
            @RequestParam("message") String message,
            @RequestParam("type") Notification.NotificationType type,
            @RequestParam("receiverId") Long receiverId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // Get sender (teacher) and receiver
        Teacher teacher = (Teacher) userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Receiver not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Create a new notification
        Notification notification = new Notification(message, title, type, teacher, receiver);

        // Save the notification
        notificationRepository.save(notification);

        // Redirect to some success page or the previous page
        return "redirect:/teacher/dashboard"; // Adjust as needed
    }



}
