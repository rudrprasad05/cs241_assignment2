package com.group6.assignment2.controllers.subject;

import com.group6.assignment2.config.Link;
import com.group6.assignment2.entity.*;
import com.group6.assignment2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.Arrays;
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
    @Autowired
    private StudentRepository studentRepository;

    public GetSubjectController() {
        sideNavLinks = Link.addLinks("admin");
    }

    @GetMapping("/admin/subjects")
    public String showSubjectsPage(
            Model model,
            @RequestParam(defaultValue = "1") int page
    ) {
        if(page <= 0 ){
            return "redirect:/admin/subjects";
        }
        // Fetch all subjects from the database
        List<Teacher> allTeachers = teacherRepository.findAll();
        List<Integer> pageNumbers = new ArrayList<>();

        double subjectCount = subjectRepository.count();
        double pageSize = 10L;
        int pages = (int) Math.ceil(subjectCount / pageSize);

        for(int i = 1; i <= pages; i++) {
            pageNumbers.add(i);
        }
        boolean prevDisabled = page == 1;
        boolean nextDisabled = page >= pages;

        Page<Subject> allSubjectsPage = subjectRepository.findAll(PageRequest.of(page -1, (int) pageSize));

        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("subjects", allSubjectsPage);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("currentPage", page);
        model.addAttribute("prevDisabled", prevDisabled);
        model.addAttribute("nextDisabled", nextDisabled);
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
        List<Teacher> teachers = teacherRepository.findAll();
        List<Student> students = studentRepository.findStudentsNotEnrolledInSubject(subject.getId());

        // Check if subject exists


        // Add subject to the model to pass to the view
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("subject", subject);
        model.addAttribute("students", students);
        model.addAttribute("periods", periodsList);
        model.addAttribute("teachers", teachers);
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
        SubjectClass subjectClass = subjectClassRepository.findByCode(classId);
        Session session = sessionRepository.findBySessionId(aId);
        List<Attendance> attendanceList = session.getAttendanceRecords();
        List<Attendance.AttendanceType> attendanceTypeList = new ArrayList<Attendance.AttendanceType>(Arrays.stream(Attendance.AttendanceType.values()).toList());

        // Add subject to the model to pass to the view
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("subjectClass", subjectClass);
        model.addAttribute("attendanceTypeList", attendanceTypeList);
        model.addAttribute("classSession", session);

        return "admin/attendance-details";
    }




}
