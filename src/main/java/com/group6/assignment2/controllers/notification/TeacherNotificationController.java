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
public class TeacherNotificationController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    static List<Link> sideNavLinks = new ArrayList<>();


//    @GetMapping("/teacher/notifications")
//    public String teacherSubject(Model model, @AuthenticationPrincipal UserDetails userDetails){
//
//        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        List<Notification> receivedNotifications = user.getReceivedNotifications();
//
//        addLinks();
//        model.addAttribute("receivedNotifications", receivedNotifications);
//        model.addAttribute("sideNavLinks", sideNavLinks);
//        model.addAttribute("notificationTypes", Arrays.asList(Notification.NotificationType.values()));
//
//
//        return "teacher/notifications";  // Refers to src/main/resources/templates/user/dashboard.html
//    }

    @GetMapping("/teacher/notifications/send")
    public String teacherNotification(Model model, @AuthenticationPrincipal UserDetails userDetails){

        Teacher teacher = (Teacher) userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Notification> receivedNotifications = teacher.getReceivedNotifications();

        List<User> receivers = getEligibleReceivers(teacher);

        addLinks();
        model.addAttribute("receivers", receivers);
        model.addAttribute("receivedNotifications", receivedNotifications);
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("notificationTypes", Arrays.asList(Notification.NotificationType.values()));


        return "teacher/sendNotifications";  // Refers to src/main/resources/templates/user/dashboard.html
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

    // Helper method to get eligible receivers (students and admins)
    private List<User> getEligibleReceivers(Teacher teacher) {
        // Fetch students and admins associated with subjects the teacher is teaching
        List<Subject> subjects = subjectRepository.findByTeacher(teacher.getId());

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

    private static void addLinks() {
        sideNavLinks.clear();
        sideNavLinks.add(new Link("/teacher/dashboard", "Subjects"));
        sideNavLinks.add(new Link("/teacher/notifications", "Notifications"));
        sideNavLinks.add(new Link("/teacher/invite-teacher", "Invite Teacher"));
    }
}
