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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
        roles.remove(Role.PARENT);

        model.addAttribute("sideNavLinks", sideNavLinks);

        model.addAttribute("roles", roles);
        return "admin/invite-user";
    }

    @GetMapping("/admin/invite-parent")
    public String inviteParent(Model model) {
        List<User> allStudents = userRepository.findAllByRole(Role.STUDENT);

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("allStudents", allStudents);
        return "admin/invite-parent";
    }



}

