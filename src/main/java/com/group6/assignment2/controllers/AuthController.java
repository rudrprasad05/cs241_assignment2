package com.group6.assignment2.controllers;

import com.group6.assignment2.entity.*;
import com.group6.assignment2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;
import java.util.UUID;

@Controller
public class AuthController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private InviteLinkRepository inviteLinkRepository;

    @GetMapping("/auth/login")
    public String showLoginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            // Redirect to appropriate dashboard based on role
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            switch (role) {
                case "ROLE_ADMIN":
                    return "redirect:/admin/dashboard";
                case "ROLE_STUDENT":
                    return "redirect:/student/dashboard";
                case "ROLE_TEACHER":
                    return "redirect:/teacher/dashboard";
                // Add more roles if necessary
                default:
                    return "/";

            }
        }
        return "auth/login";
    }

    @GetMapping("/auth/logout")
    public String showLogoutPage() {
        return "auth/login";
    }

    @GetMapping("/auth/application")
    public String showApplicationPage(Model model) {
        return "auth/application";
    }

    @GetMapping("/auth/application-success")
    public String showApplicationSuccessPage(Model model, @RequestParam String id) {
        model.addAttribute("id", id);
        return "auth/application-success";
    }

    @PostMapping("/auth/application")
    public String submitApplication(@RequestParam("fName") String fName, @RequestParam("lName") String lName, @RequestParam("email") String personalEmail, @RequestParam("password") String password, @RequestParam(value = "image", required = false) MultipartFile image, Model model) {

        String studentId = generateStudentId();
        String studentEmail = studentId + "@student.com";
        Student student = new Student(studentId, fName, lName, studentEmail, passwordEncoder.encode(password), studentId);
        student.setPersonalEmail(personalEmail);
        studentRepository.save(student);

        String inviteLinkCode = UUID.randomUUID().toString().replace("-", "").substring(0, 32);  // Generate a 32-character random string
        InviteLink inviteLink = new InviteLink(inviteLinkCode);
        inviteLink.setUser(student);
        inviteLinkRepository.save(inviteLink);

        String message = "You have been invited to join us on our attendance management system";
        String title = "Welcome!!";
        Notification.NotificationType notificationType = Notification.NotificationType.INFO;
        User admin =  userRepository.findOneByRole(Role.ADMIN);
        Notification notification = new Notification(message, title, notificationType, admin, student);
        notificationRepository.save(notification);

        Email email = getEmail(personalEmail, inviteLinkCode, studentId);
        emailRepository.save(email);
        EmailController.SendAutomatedEmail(email);

        model.addAttribute("message", "Application submitted successfully. Your student ID is " + studentId);

        return "redirect:/auth/application-success?id=" + studentId;
    }

    private static Email getEmail(String personalEmail, String inviteLinkCode, String studentId) {
        String header = "Hey there! Welcome to our Attendance Tracker";
        String body = "Use this link to access the login page: <a href='localhost:8080/auth/invite" + inviteLinkCode + "'>Invite Link</a> " +
                "<p>Cant see the code? Copy paste this into your browser http://localhost:8080/auth/invite/" + inviteLinkCode + "</p>" +
                "<p>Your username and student ID is: " + studentId + "</p>";
        String subject = "Invitation Link";

        Email email = new Email(header, body, subject, personalEmail);
        return email;
    }

    private String generateStudentId() {
        Random random = new Random();
        StringBuilder studentId = new StringBuilder("S");
        for (int i = 0; i < 8; i++) {
            studentId.append(random.nextInt(9) + 1); // Ensuring the number is between 1 and 9
        }
        String sId  = studentId.toString();

        if(studentRepository.findByStudentId(sId) != null) {
            generateStudentId();
        }
        else {
            return sId;
        }
        return sId;
    }

    // Helper method to generate student ID

}
