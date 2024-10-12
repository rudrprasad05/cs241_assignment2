package com.group6.assignment2.controllers;

import com.group6.assignment2.entity.Student;
import com.group6.assignment2.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;

@Controller
public class AuthController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/auth/login")
    public String showLoginPage() {
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
    public String submitApplication(@RequestParam("fName") String fName, @RequestParam("lName") String lName, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam(value = "image", required = false) MultipartFile image, Model model) {

        String studentId = generateStudentId();
        String studentEmail = studentId + "@student.com";
        Student student = new Student(studentId, fName, lName, studentEmail, passwordEncoder.encode(password), studentId);

        studentRepository.save(student);
        model.addAttribute("message", "Application submitted successfully. Your student ID is " + studentId);

        return "redirect:/auth/application-success?id=" + studentId;
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
