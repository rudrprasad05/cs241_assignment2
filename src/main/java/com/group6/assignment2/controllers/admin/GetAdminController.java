package com.group6.assignment2.controllers.admin;

import com.group6.assignment2.config.Link;
import com.group6.assignment2.config.RedirectionConfig;
import com.group6.assignment2.controllers.EmailController;
import com.group6.assignment2.entity.*;
import com.group6.assignment2.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.*;

@Controller
public class GetAdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private InviteLinkRepository inviteLinkRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    static List<Link> sideNavLinks = new ArrayList<>();

    public GetAdminController() {
        sideNavLinks = Link.addLinks("admin");
    }

    @GetMapping("/admin")
    public String routeDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("currentUser");

        return RedirectionConfig.RedirectToDashboard(user, null, null);
    }

    @GetMapping("/admin/applications")
    public String adminApplications(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("pageTitle", "Admin Dashboard");
        model.addAttribute("message", "Welcome to the Admin Dashboard");

        return "admin/applications";

    }


    @GetMapping("/admin/dashboard")
    public String userDashboard(Model model) {
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("pageTitle", "Admin Dashboard");
        model.addAttribute("message", "Welcome to the Admin Dashboard");
        return "admin/dashboard";  // Refers to src/main/resources/templates/user/dashboard.html
    }

    // Show invite teacher page
    @GetMapping("/admin/invite-user")
    public String inviteUser(Model model) {
        List<Role> roles = new ArrayList<>(List.of(Role.values()));
        List<User> allStudents = userRepository.findAllByRole(Role.STUDENT);
        roles.remove(Role.PARENT);

        model.addAttribute("sideNavLinks", sideNavLinks);

        model.addAttribute("allStudents", allStudents);
        model.addAttribute("roles", roles);
        return "admin/invite-user";
    }

    @GetMapping("/admin/notifications")
    public String adminNotification(Model model, @AuthenticationPrincipal UserDetails userDetails){
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Notification> receivedNotifications = user.getReceivedNotifications();

        model.addAttribute("receivedNotifications", receivedNotifications);
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("notificationTypes", Arrays.asList(Notification.NotificationType.values()));

        return "/notifications";  // Refers to src/main/resources/templates/user/dashboard.html
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


}

