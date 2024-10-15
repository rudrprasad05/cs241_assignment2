package com.group6.assignment2.controllers.subjectClass;

import com.group6.assignment2.entity.Subject;
import com.group6.assignment2.entity.SubjectClass;
import com.group6.assignment2.entity.Teacher;
import com.group6.assignment2.repository.SubjectClassRepository;
import com.group6.assignment2.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller

public class SubjectClassController {

    @Autowired
    private SubjectClassRepository subjectClassRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @PostMapping("/admin/subject-class/add")
    public String addClass(RedirectAttributes redirectAttributes, @RequestParam("subjectCode") String subjectCode, @RequestParam("day") String day, @RequestParam("time") String time, @RequestParam("roomCode") String description, Model model) {

        Subject subject = subjectRepository.findByCode(subjectCode);
        SubjectClass subjectClass = new SubjectClass(day, time, description, subject);
        subjectClassRepository.save(subjectClass);

        redirectAttributes.addFlashAttribute("toastMessage", "New class was created");
        redirectAttributes.addFlashAttribute("toastType", "success");  // You can send 'success', 'error', etc.


        return "redirect:/admin/subjects/" + subjectCode;

    }
}
