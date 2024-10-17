package com.group6.assignment2.controllers.subject;

import com.group6.assignment2.config.Link;
import com.group6.assignment2.entity.*;
import com.group6.assignment2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;


@Controller

public class GetSubjectController {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private TeacherRepository userRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private SubjectClassRepository subjectClassRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    static List<Link> sideNavLinks = new ArrayList<>();
    @Autowired
    private PeriodRepository periodRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private SessionRepository sessionRepository;

    public GetSubjectController() {
        sideNavLinks = Link.addLinks("admin");
    }

    @GetMapping("/admin/subjects")
    public String showSubjectsPage(Model model) {
        // Fetch all subjects from the database
        List<Teacher> allTeachers = teacherRepository.findAll();
        List<Subject> allSubjects = subjectRepository.findAll();

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("subjects", allSubjects);
        model.addAttribute("teachers", allTeachers);

        return "admin/subjects";
    }


    @GetMapping("/admin/subject/add")
    public String addSubjectModal(Model model) {
        return "modals/addSubjectModal";
    }


    @GetMapping("/admin/subjects/{subject_code}")
    public String viewSubjectDetails(@PathVariable("subject_code") String subject_code, Model model) {
        // Fetch subject by subject_name from the database
        Subject subject = subjectRepository.findByCode(subject_code);

        if (subject == null) {
            return "error/error-404"; // or handle it appropriately
        }

        List<SubjectClass> subjectClass = subjectClassRepository.findBySubjectCode(subject_code);
        List<Enrollment> enrollments = subject.getEnrollments();
        List<Period> periodsList = periodRepository.findAll();

        // Check if subject exists


        // Add subject to the model to pass to the view
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("subject", subject);
        model.addAttribute("periods", periodsList);
        model.addAttribute("subjectClass", subjectClass);

        return "admin/subject-details";
    }

    @GetMapping("/admin/subjects/{subject_code}/{classId}")
    public String viewAttendanceDetails(@PathVariable("classId") String classId, @PathVariable("subject_code") String subject_code, Model model) {
        // Fetch subject by subject_name from the database
        SubjectClass subjectClass = subjectClassRepository.findByCode(classId);
        // Check if subject exists
        if (subjectClass == null) {
            return "error/error-404"; // or handle it appropriately
        }

        List<Session> sessions = subjectClass.getSessions();


        // Add subject to the model to pass to the view
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("sessions", sessions);
        model.addAttribute("subjectClass", subjectClass);

        return "admin/class-details";
    }

    @GetMapping("/admin/subjects/{subject_code}/{classId}/attendance/{aId}")
    public String viewClassDetails(@PathVariable("classId") String classId, @PathVariable("subject_code") String subject_code, @PathVariable("aId") String aId, Model model) {
        // Fetch subject by subject_name from the database
        Session session = sessionRepository.findBySessionId(aId);
        List<Attendance> attendanceList = session.getAttendanceRecords();

        // Add subject to the model to pass to the view
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("session", session);

        return "admin/attendance-details";
    }




}
