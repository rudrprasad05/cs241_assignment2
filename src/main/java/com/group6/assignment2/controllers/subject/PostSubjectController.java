package com.group6.assignment2.controllers.subject;

import com.group6.assignment2.config.Link;
import com.group6.assignment2.entity.*;
import com.group6.assignment2.repository.NotificationRepository;
import com.group6.assignment2.repository.SubjectClassRepository;
import com.group6.assignment2.repository.SubjectRepository;
import com.group6.assignment2.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;


@Controller

public class PostSubjectController {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private TeacherRepository userRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private SubjectClassRepository subjectClassRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    static List<Link> sideNavLinks = new ArrayList<>();

    public PostSubjectController() {
        sideNavLinks = Link.addLinks("admin");
    }

    @PostMapping("/admin/subjects/add")
    public String addSubject(RedirectAttributes redirectAttributes, @RequestParam("name") String name, @RequestParam("code") String code, @RequestParam("description") String description, @RequestParam("teacherId") Long teacherId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Create and save the new subject
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new IllegalArgumentException("Invalid teacher ID: " + teacherId));

        Subject subject = new Subject(code, name, teacher, description);
        subjectRepository.save(subject);

        String message = "You have been appointed as the new teacher for the subject " + code;
        String title = "Welcome!!";
        Notification.NotificationType notificationType = Notification.NotificationType.INFO;
        User sender =  userRepository.findByUsername(userDetails.getUsername());

        Notification notification = new Notification(message, title, notificationType, sender, teacher);
        notificationRepository.save(notification);

        redirectAttributes.addFlashAttribute("toastMessage", "New subject was created");
        redirectAttributes.addFlashAttribute("toastType", "success");  // You can send 'success', 'error', etc.


        return "redirect:/admin/subjects";

    }

}
