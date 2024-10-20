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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller

public class AttendanceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttendanceController.class);

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectClassRepository subjectClassRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @PostMapping("/teacher/attendance/update")
    public String addClass(
            RedirectAttributes redirectAttributes,
            @RequestParam("attendanceId") String attendanceId,
            @RequestParam("attendanceStatus") Attendance.AttendanceType attendanceStatus,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest request
    ) {
        // Find attendance record by ID
        Attendance attendance = attendanceRepository.findByAttendanceId(attendanceId);

        // Update the attendance status with the selected value
        attendance.setPresent(attendanceStatus);

        Student student = attendance.getStudent();
        List<Parent> studentParents = student.getParents();

        String message = "Your child was marked as " + attendanceStatus;
        String title = "Attention!";
        Notification.NotificationType notificationType = Notification.NotificationType.WARNING;
        User admin =  userRepository.findOneByRole(Role.ADMIN);

        for (Parent parent : studentParents) {
            System.out.println(parent.getParentId());
            Notification notification = new Notification(message, title, notificationType, admin, parent);
            notificationRepository.save(notification);
            System.out.println("sent");
        }

        // Save the updated attendance record
        attendanceRepository.save(attendance);

        redirectAttributes.addFlashAttribute("toastMessage", "Attendance Updated Successfully");
        redirectAttributes.addFlashAttribute("toastType", "success");  // You can send 'success', 'error', etc.


        // Redirect back to the referring page
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PostMapping("/admin/attendance/update")
    public String updateAttendance(
            RedirectAttributes redirectAttributes,
            @RequestParam("attendanceId") String attendanceId,
            @RequestParam("attendanceStatus") Attendance.AttendanceType attendanceStatus,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest request
    ) {
        // Find attendance record by ID
        Attendance attendance = attendanceRepository.findByAttendanceId(attendanceId);

        // Update the attendance status with the selected value
        attendance.setPresent(attendanceStatus);

        Student student = attendance.getStudent();
        List<Parent> studentParents = student.getParents();

        String message = "Your child was marked as " + attendanceStatus;
        String title = "Attention!";
        Notification.NotificationType notificationType = Notification.NotificationType.WARNING;
        User admin =  userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        for (Parent parent : studentParents) {
            System.out.println(parent.getParentId());
            Notification notification = new Notification(message, title, notificationType, admin, parent);
            notificationRepository.save(notification);
            System.out.println("sent");
        }


        // Save the updated attendance record
        attendanceRepository.save(attendance);

        redirectAttributes.addFlashAttribute("toastMessage", "Attendance Updated Successfully");
        redirectAttributes.addFlashAttribute("toastType", "success");  // You can send 'success', 'error', etc.

        // Redirect back to the referring page
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }




}