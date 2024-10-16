package com.group6.assignment2.controllers.enrollment;

import com.group6.assignment2.entity.*;
import com.group6.assignment2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

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
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @PostMapping("/student/enrollment/new")
    public String addClass(RedirectAttributes redirectAttributes, @RequestParam("subjectClassId") String subjectClassId, @AuthenticationPrincipal UserDetails userDetails) {

        Student student = studentRepository.findByUsername(userDetails.getUsername());
        Admin admin = adminRepository.findByUsername("admin");
        SubjectClass subjectClass = subjectClassRepository.findByCode(subjectClassId);
        Subject subject = subjectClass.getSubject();

        Enrollment enrollment = new Enrollment(student, subjectClass, subject);
        enrollmentRepository.save(enrollment);

        String title = "A new enrollment has been created!";
        String message = "Check the subject - " + subject.getCode() + "\nUsername - " + student.getUsername();
        Notification notification = new Notification(message, title, Notification.NotificationType.INFO, student, admin);

        notificationRepository.save(notification);

        redirectAttributes.addFlashAttribute("toastMessage", "You have joined this class");
        redirectAttributes.addFlashAttribute("toastType", "success");  // You can send 'success', 'error', etc.


        return "redirect:/student/subjects/" + subjectClass.getSubject().getCode();

    }

    @PostMapping("/admin/enrollment/change-status/accepted")
    public String accepted(RedirectAttributes redirectAttributes, @RequestParam("enrollmentId") String enrollmentId) {
        System.out.println("hit");
        Long id = Long.parseLong(enrollmentId);
        Enrollment enrollment = enrollmentRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Enrollment not found"));
        enrollment.setAccepted(Enrollment.EnrollmentStatus.ACCEPTED);

        enrollmentRepository.save(enrollment);

        List<Session> sessionsList = enrollment.getSubjectClass().getSessions();

        for(Session session : sessionsList) {
            Attendance attendance = new Attendance(Attendance.AttendanceType.NOT_MARKED, "n/a", enrollment.getStudent(), session);
            attendanceRepository.save(attendance);
        }

        redirectAttributes.addFlashAttribute("toastMessage", "Enrollment Status changed to accepted");
        redirectAttributes.addFlashAttribute("toastType", "success");  // You can send 'success', 'error', etc.


        return "redirect:/admin/subjects/" + enrollment.getSubject().getCode();

    }

    @PostMapping("/admin/enrollment/change-status/rejected")
    public String rejected(RedirectAttributes redirectAttributes, @RequestParam("enrollmentId") String enrollmentId) {

        Long id = Long.parseLong(enrollmentId);
        Enrollment enrollment = enrollmentRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Enrollment not found"));
        enrollment.setAccepted(Enrollment.EnrollmentStatus.REJECTED);

        enrollmentRepository.save(enrollment);

        List<Session> sessionsList = enrollment.getSubjectClass().getSessions();
        for(Session session : sessionsList) {
            for(Attendance a : session.getAttendanceRecords()){
                if(Objects.equals(a.getStudent(), enrollment.getStudent())){
                    System.out.println("delelledddd");
                    attendanceRepository.delete(a);
                }
            }
        }
//        attendanceRepository.deleteByStudentAndSessions(enrollment.getStudent(), enrollment.getSubjectClass().getSubjectClasses());

        redirectAttributes.addFlashAttribute("toastMessage", "Enrollment Status changed to rejected");
        redirectAttributes.addFlashAttribute("toastType", "success");  // You can send 'success', 'error', etc.


        return "redirect:/admin/subjects/" + enrollment.getSubject().getCode();

    }

}