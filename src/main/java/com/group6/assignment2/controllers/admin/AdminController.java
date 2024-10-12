package com.group6.assignment2.controllers.admin;

import com.group6.assignment2.config.Link;
import com.group6.assignment2.config.RedirectionConfig;
import com.group6.assignment2.entity.InviteLink;
import com.group6.assignment2.entity.Subject;
import com.group6.assignment2.entity.Teacher;
import com.group6.assignment2.entity.User;
import com.group6.assignment2.repository.InviteLinkRepository;
import com.group6.assignment2.repository.SubjectRepository;
import com.group6.assignment2.repository.TeacherRepository;
import com.group6.assignment2.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private InviteLinkRepository inviteLinkRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    static List<Link> sideNavLinks = new ArrayList<>();

    public AdminController() {
        addLinks();
    }

    private static void addLinks() {
        sideNavLinks.clear();
        sideNavLinks.add(new Link("/admin/subjects", "Subjects"));
        sideNavLinks.add(new Link("/admin/invite-teachers", "Invite Teacher"));
    }

    @GetMapping("/admin")
    public String routeDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("currentUser");

        return RedirectionConfig.RedirectToDashboard(user, null, null);
    }

    @GetMapping("/admin/dashboard")
    public String userDashboard(Model model) {
        addLinks();
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("pageTitle", "Admin Dashboard");
        model.addAttribute("message", "Welcome to the Admin Dashboard");
        return "admin/dashboard";  // Refers to src/main/resources/templates/user/dashboard.html
    }

    // Show invite teacher page
    @GetMapping("/admin/invite-teachers")
    public String inviteTeachersPage(Model model) {
        List<Subject> allSubjects = subjectRepository.findSubjectsWithoutTeacher();
        addLinks();

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("subjects", allSubjects);
        return "admin/invite-teacher";
    }

    // Handle form submission to invite teachers
    @PostMapping("/admin/invite-teachers")
    public void inviteTeacher(@RequestParam("password") String password, @RequestParam("fName") String fName, @RequestParam("lName") String lName, Model model) {

        String tId = generateTeacherId();
        String teacherEmail = tId + "@teacher.com";

        // Create a new user with role = 2 (teacher)
        Teacher teacher = new Teacher(tId, teacherEmail, passwordEncoder.encode(password), fName, lName);
        teacherRepository.save(teacher);

        // Create an invite link and associate it with the teacher
        InviteLink inviteLink = new InviteLink();
        inviteLink.setTeacher(teacher);
        inviteLinkRepository.save(inviteLink);

        model.addAttribute("inviteLink", "/invite/" + inviteLink.getInviteCode());
    }



    private String generateTeacherId() {
        Random random = new Random();
        StringBuilder teacherId = new StringBuilder("T");
        for (int i = 0; i < 8; i++) {
            teacherId.append(random.nextInt(9) + 1);  // Ensuring numbers are between 1 and 9
        }

        String tId = teacherId.toString();

        // Check if the ID already exists, if it does, recursively generate a new one
        if (teacherRepository.findByTeacherId(tId) != null) {
            return generateTeacherId();  // Return the result from the recursive call
        } else {
            return tId;  // Return the generated ID if it's unique
        }
    }

}

