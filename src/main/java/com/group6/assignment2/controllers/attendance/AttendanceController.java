package com.group6.assignment2.controllers.attendance;

import com.group6.assignment2.controllers.teacher.TeacherController;
import com.group6.assignment2.entity.*;
import com.group6.assignment2.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller

public class AttendanceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttendanceController.class);

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

//    @PostMapping("/teacher/attendance/update")
//    public String addClass(@RequestParam("attendanceId") String attendanceId, @RequestParam(value = "isPresent", required = false) boolean isPresent, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
//        Attendance attendance = attendanceRepository.findByAttendanceId(attendanceId);
//        if(isPresent) {
//            attendance.setPresent(Attendance.ATTENDANCE.PRESENT);
//        }
//        else{
//            attendance.setPresent(Attendance.ATTENDANCE.ABSENT);
//        }
//
//        attendanceRepository.save(attendance);
//
//        String referer = request.getHeader("Referer");
//        return "redirect:" + referer;
//    }

    @PostMapping("/teacher/attendance/update")
    public String addClass(
            @RequestParam("attendanceId") String attendanceId,
            @RequestParam("attendanceStatus") Attendance.ATTENDANCE attendanceStatus,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest request
    ) {
        // Find attendance record by ID
        Attendance attendance = attendanceRepository.findByAttendanceId(attendanceId);

        // Update the attendance status with the selected value
        attendance.setPresent(attendanceStatus);

        // Save the updated attendance record
        attendanceRepository.save(attendance);

        // Redirect back to the referring page
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }




}