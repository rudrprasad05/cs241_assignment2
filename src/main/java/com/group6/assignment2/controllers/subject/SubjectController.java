package com.group6.assignment2.controllers.subject;

import com.group6.assignment2.config.Link;
import com.group6.assignment2.entity.Subject;
import com.group6.assignment2.entity.Teacher;
import com.group6.assignment2.repository.SubjectRepository;
import com.group6.assignment2.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


@Controller

public class SubjectController {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    static List<Link> sideNavLinks = new ArrayList<>();

    @GetMapping("/admin/subjects")
    public String showSubjectsPage(Model model) {
        // Fetch all subjects from the database
        addLinks();
        List<Teacher> allTeachers = teacherRepository.findAll();
        List<Subject> allSubjects = subjectRepository.findAll();

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("subjects", allSubjects);
        model.addAttribute("teachers", allTeachers);

        return "admin/subjects";
    }

    @GetMapping("/admin/subjects/{subject_name}")
    public String viewSubjectDetails(@PathVariable("subject_name") String subjectName, Model model) {
        // Fetch subject by subject_name from the database
        Subject subject = subjectRepository.findByName(subjectName);

        // Check if subject exists
        if (subject == null) {
            return "error/error-404"; // or handle it appropriately
        }

        // Add subject to the model to pass to the view
        addLinks();
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("subject", subject);

        return "admin/subject-details";
    }

    @PostMapping("/admin/subjects/add")
    public String addSubject(@RequestParam("name") String name, @RequestParam("code") String code, @RequestParam("description") String description, @RequestParam("teacherId") Long teacherId, Model model) {
        // Create and save the new subject
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new IllegalArgumentException("Invalid teacher ID: " + teacherId));

        Subject subject = new Subject(code, name, teacher, description);
        subjectRepository.save(subject);

        return "redirect:/admin/subjects";

    }

    public static void addLinks() {
        sideNavLinks.clear();
        sideNavLinks.add(new Link("/admin/subjects", "Subjects"));
        sideNavLinks.add(new Link("/admin/invite-teachers", "Invite Teacher"));
    }
}
