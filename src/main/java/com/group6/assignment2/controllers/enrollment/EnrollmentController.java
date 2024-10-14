package com.group6.assignment2.controllers.enrollment;

import com.group6.assignment2.entity.*;
import com.group6.assignment2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    @Autowired
    private AttendanceRepository attendanceRepository;

    @PostMapping("/student/enrollment/new")
    public String addClass(@RequestParam("subjectClassId") String subjectClassId, @AuthenticationPrincipal UserDetails userDetails) {

        Student student = studentRepository.findByUsername(userDetails.getUsername());
        SubjectClass subjectClass = subjectClassRepository.findByCode(subjectClassId);

        Enrollment enrollment = new Enrollment(student, subjectClass);
        enrollmentRepository.save(enrollment);


        List<Session> sessionsList = subjectClass.getSessions();

        for(Session session : sessionsList) {
            Attendance attendance = new Attendance(false, "n/a", student, session);
            attendanceRepository.save(attendance);
        }

        return "redirect:/student/subjects/" + subjectClass.getSubject().getCode();

    }

}