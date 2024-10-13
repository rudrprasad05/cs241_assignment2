package com.group6.assignment2.controllers.enrollment;

import com.group6.assignment2.entity.Enrollment;
import com.group6.assignment2.entity.Student;
import com.group6.assignment2.entity.Subject;
import com.group6.assignment2.entity.SubjectClass;
import com.group6.assignment2.repository.EnrollmentRepository;
import com.group6.assignment2.repository.StudentRepository;
import com.group6.assignment2.repository.SubjectClassRepository;
import com.group6.assignment2.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class EnrollmentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectClassRepository subjectClassRepository;

    @PostMapping("/student/enrollment/new")
    public String addClass(@RequestParam("subjectClassId") String subjectClassId, @AuthenticationPrincipal UserDetails userDetails) {

        Student student = studentRepository.findByUsername(userDetails.getUsername());
        SubjectClass subjectClass = subjectClassRepository.findByCode(subjectClassId);

        Enrollment enrollment = new Enrollment(student, subjectClass);
        enrollmentRepository.save(enrollment);

        return "redirect:/student/subjects/" + subjectClass.getSubject().getCode();

    }

}