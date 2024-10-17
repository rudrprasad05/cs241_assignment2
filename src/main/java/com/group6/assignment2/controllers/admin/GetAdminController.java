package com.group6.assignment2.controllers.admin;

import com.group6.assignment2.config.Link;
import com.group6.assignment2.config.RedirectionConfig;
import com.group6.assignment2.entity.*;
import com.group6.assignment2.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private SubjectClassRepository subjectClassRepository;

    public GetAdminController() {
        sideNavLinks = Link.addLinks("admin");
    }

    @GetMapping("/admin")
    public String routeDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("currentUser");

        return RedirectionConfig.RedirectToDashboard(user, null, null);
    }

    @GetMapping("/admin/users")
    public String adminApplications(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<User> allUsers = userRepository.findAll();
        allUsers.sort(Comparator.comparing(User::getId));

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("pageTitle", "Admin Dashboard");
        model.addAttribute("message", "Welcome to the Admin Dashboard");

        return "/admin/users";

    }

    @GetMapping("/admin/users/{id}")
    public String adminUsers(@PathVariable("id") Long id, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
       User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));

       Student s;
       Teacher t;
       Parent p;
       Admin a;
       if(user instanceof Student) {
           s = (Student) user;
           List<SubjectClass> classes = s.getEnrollments().stream()
                   .map(Enrollment::getSubjectClass)
                   .toList();
           List<Parent> parents = s.getParents();
           model.addAttribute("user", s);
           model.addAttribute("type", "student");
           model.addAttribute("parents", parents);
           model.addAttribute("classes", classes);
       }
       else if(user instanceof Teacher) {
           t = (Teacher) user;
           model.addAttribute("user", t);
           model.addAttribute("type", "teacher");

       }
       else if (user instanceof Admin) {
           a = (Admin) user;
           model.addAttribute("user", a);
           model.addAttribute("type", "admin");

       }
       else if(user instanceof Parent) {
           p = (Parent) user;
           model.addAttribute("user", p);
           model.addAttribute("type", "parent");

       }


        model.addAttribute("sideNavLinks", sideNavLinks);

        model.addAttribute("pageTitle", "Admin Dashboard");
        model.addAttribute("message", "Welcome to the Admin Dashboard");

        return "/admin/user-details";

    }

    @GetMapping("/admin/users/{id}/edit")
    public String adminUsersEdit(@PathVariable("id") Long id, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Student s;
        Teacher t;
        Parent p;
        Admin a;

        List<Role> roles = new ArrayList<>(Arrays.stream(Role.values()).toList());
        if(user instanceof Student) {
            s = (Student) user;
            List<SubjectClass> classes = s.getEnrollments().stream()
                    .map(Enrollment::getSubjectClass)
                    .toList();
            List<Parent> parents = s.getParents();
            model.addAttribute("user", s);
            model.addAttribute("type", "student");
            model.addAttribute("parents", parents);
            model.addAttribute("classes", classes);
        }
        else if(user instanceof Teacher) {
            t = (Teacher) user;
            model.addAttribute("user", t);
            model.addAttribute("type", "teacher");

        }
        else if (user instanceof Admin) {
            a = (Admin) user;
            model.addAttribute("user", a);
            model.addAttribute("type", "admin");

        }
        else if(user instanceof Parent) {
            p = (Parent) user;
            model.addAttribute("user", p);
            model.addAttribute("type", "parent");

        }

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("roles", roles);
        model.addAttribute("pageTitle", "Admin Dashboard");
        model.addAttribute("message", "Welcome to the Admin Dashboard");

        return "/admin/edit-user-details";

    }


    @GetMapping("/admin/dashboard")
    public String userDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        long userCount = studentRepository.count();
        long notificationCount = notificationRepository.countUnseenNotificationsByReceiver(user.getId());
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("pageTitle", "Admin Dashboard");
        model.addAttribute("user", user);
        model.addAttribute("notificationCount", notificationCount);
        model.addAttribute("userCount", userCount);
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

