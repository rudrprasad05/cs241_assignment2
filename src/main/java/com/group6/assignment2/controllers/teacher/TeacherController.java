package com.group6.assignment2.controllers.teacher;

import com.group6.assignment2.config.Link;
import com.group6.assignment2.entity.Subject;
import com.group6.assignment2.entity.SubjectClass;
import com.group6.assignment2.entity.Teacher;
import com.group6.assignment2.entity.User;
import com.group6.assignment2.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class TeacherController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectClassRepository subjectClassRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private InviteLinkRepository inviteLinkRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    static List<Link> sideNavLinks = new ArrayList<>();


    @GetMapping("/teacher/dashboard")
    public String userDashboard(Model model) {

        Teacher teacher = getLoggedTeacher();  // Implement method to get logged-in teacher
        assert teacher != null;
        List<Subject> subjects = teacher.getSubjects();
        model.addAttribute("subjects", subjects);

        addLinks();
        model.addAttribute("sideNavLinks", sideNavLinks);


        return "teacher/dashboard";  // Refers to src/main/resources/templates/user/dashboard.html
    }

    @GetMapping("/teacher/subjects/{code}")
    public String teacherSubject(Model model, @PathVariable("code") String code, @AuthenticationPrincipal UserDetails userDetails) {

        Subject subject = subjectRepository.findByCode(code);
        User teacher = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        addLinks();
        model.addAttribute("subject", subject);
        model.addAttribute("sideNavLinks", sideNavLinks);


        return "teacher/subject-details";  // Refers to src/main/resources/templates/user/dashboard.html
    }

    @GetMapping("/teacher/subjects/{code}/{classId}")
    public String teacherClassSubject(Model model, @PathVariable("code") String code, @PathVariable("classId") String classId, @AuthenticationPrincipal UserDetails userDetails) {

        Subject subject = subjectRepository.findByCode(code);
        SubjectClass subjectClass = subjectClassRepository.findByCode(classId);
        User teacher = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        addLinks();
        model.addAttribute("subject", subject);
        model.addAttribute("subjectClass", subjectClass);
        model.addAttribute("sideNavLinks", sideNavLinks);


        return "teacher/class-details";  // Refers to src/main/resources/templates/user/dashboard.html
    }

    private Teacher getLoggedTeacher() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails)principal).getUsername();
            return teacherRepository.findByUsername(username);
        }

        return null;
    }

    private static void addLinks() {
        sideNavLinks.clear();
        sideNavLinks.add(new Link("/teacher/dashboard", "Subjects"));
        sideNavLinks.add(new Link("/teacher/invite-teacher", "Invite Teacher"));
    }


}

