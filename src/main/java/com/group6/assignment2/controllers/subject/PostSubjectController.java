package com.group6.assignment2.controllers.subject;

import com.group6.assignment2.config.Link;
import com.group6.assignment2.entity.*;
import com.group6.assignment2.repository.*;
import jakarta.servlet.http.HttpServletRequest;
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
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    static List<Link> sideNavLinks = new ArrayList<>();
    @Autowired
    private StudentRepository studentRepository;

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

    @PostMapping("/admin/subjects/changeTeacher")
    public String changeTeacher(
            RedirectAttributes redirectAttributes,
            @RequestParam("subjectId") Long subjectId,
            @RequestParam("teacherId") Long teacherId,
            HttpServletRequest request,
            Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Create and save the new subject
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new IllegalArgumentException("Invalid teacher ID: " + teacherId));
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new IllegalArgumentException("Invalid subjectId ID: " + subjectId));

        subject.setTeacher(teacher);
        subjectRepository.save(subject);

        String message = "You have been appointed as the new teacher for the subject " + subject.getCode();
        String title = "Welcome!";
        Notification.NotificationType notificationType = Notification.NotificationType.INFO;
        User sender =  userRepository.findByUsername(userDetails.getUsername());

        Notification notification = new Notification(message, title, notificationType, sender, teacher);
        notificationRepository.save(notification);

        redirectAttributes.addFlashAttribute("toastMessage", "Teacher was changed");
        redirectAttributes.addFlashAttribute("toastType", "success");  // You can send 'success', 'error', etc.

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;

    }

    @PostMapping("/admin/subjects/addNewStudent")
    public String addNewStudent(
            RedirectAttributes redirectAttributes,
            @RequestParam("studentId") Long studentId,
            @RequestParam("subjectClassId") Long subjectClassId,
            Model model,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        // Create and save the new subject
        SubjectClass subjectClass = subjectClassRepository.findById(subjectClassId).orElseThrow(() -> new IllegalArgumentException("Invalid teacher ID: " + subjectClassId));
        Student student= studentRepository.findById(studentId).orElseThrow(() -> new IllegalArgumentException("Invalid teacher ID: " + studentId));

        Enrollment enrollment = new Enrollment(student, subjectClass, subjectClass.getSubject(), Enrollment.EnrollmentStatus.ACCEPTED);
        enrollmentRepository.save(enrollment);

        String message = "You have been add to a new class for subject:  " + subjectClass.getSubject().getCode();
        String title = "Welcome!!";
        Notification.NotificationType notificationType = Notification.NotificationType.INFO;
        User sender =  userRepository.findByUsername(userDetails.getUsername());

        Notification notification = new Notification(message, title, notificationType, sender, sender);
        notificationRepository.save(notification);

        redirectAttributes.addFlashAttribute("toastMessage", "New subject was created");
        redirectAttributes.addFlashAttribute("toastType", "success");  // You can send 'success', 'error', etc.


        String referer = request.getHeader("Referer");
        return "redirect:" + referer;


    }


}
