package com.group6.assignment2.controllers.teacher;

import com.group6.assignment2.config.Link;
import com.group6.assignment2.entity.*;
import com.group6.assignment2.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class TeacherController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectClassRepository subjectClassRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private InviteLinkRepository inviteLinkRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    static List<Link> sideNavLinks = new ArrayList<>();

    public TeacherController() {
        sideNavLinks = Link.addLinks("teacher");
    }


    @GetMapping("/teacher/dashboard")
    public String userDashboard(Model model) {

        Teacher teacher = getLoggedTeacher();  // Implement method to get logged-in teacher
        assert teacher != null;
        List<Subject> subjects = teacher.getSubjects();
        model.addAttribute("subjects", subjects);

        model.addAttribute("sideNavLinks", sideNavLinks);

        return "teacher/dashboard";  // Refers to src/main/resources/templates/user/dashboard.html
    }

    @GetMapping("/teacher/subjects")
    public String teacherSubjects(Model model) {

        Teacher teacher = getLoggedTeacher();  // Implement method to get logged-in teacher
        assert teacher != null;
        List<Subject> subjects = teacher.getSubjects();
        model.addAttribute("subjects", subjects);

        model.addAttribute("sideNavLinks", sideNavLinks);

        return "teacher/my-subjects";  // Refers to src/main/resources/templates/user/dashboard.html
    }


    @GetMapping("/teacher/subjects/{code}")
    public String teacherSubject(Model model, @PathVariable("code") String code, @AuthenticationPrincipal UserDetails userDetails) {

        Subject subject = subjectRepository.findByCode(code);
        User teacher = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        model.addAttribute("subject", subject);
        model.addAttribute("sideNavLinks", sideNavLinks);


        return "teacher/subject-details";  // Refers to src/main/resources/templates/user/dashboard.html
    }



    @GetMapping("/teacher/notifications/send")
    public String teacherSendNotification(Model model, @AuthenticationPrincipal UserDetails userDetails){

        Teacher teacher = (Teacher) userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Notification> receivedNotifications = teacher.getReceivedNotifications();

        List<User> receivers = getEligibleReceivers(teacher);

        model.addAttribute("receivers", receivers);
        model.addAttribute("receivedNotifications", receivedNotifications);
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("notificationTypes", Arrays.asList(Notification.NotificationType.values()));


        return "teacher/sendNotifications";  // Refers to src/main/resources/templates/user/dashboard.html
    }


    @GetMapping("/teacher/notifications")
    public String teacherNotification(Model model, @AuthenticationPrincipal UserDetails userDetails){
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Notification> receivedNotifications = user.getReceivedNotifications();

        model.addAttribute("receivedNotifications", receivedNotifications);
        model.addAttribute("sideNavLinks", sideNavLinks);
        model.addAttribute("notificationTypes", Arrays.asList(Notification.NotificationType.values()));

        return "/notifications";  // Refers to src/main/resources/templates/user/dashboard.html
    }

    @GetMapping("/teacher/subjects/{code}/{classId}")
    public String teacherClassSubject(Model model, @PathVariable("code") String code, @PathVariable("classId") String classId, @AuthenticationPrincipal UserDetails userDetails) {

        Subject subject = subjectRepository.findByCode(code);
        SubjectClass subjectClass = subjectClassRepository.findByCode(classId);
        User teacher = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Enrollment> enrollments = subjectClass.getEnrollments();
        List<Student> enrolledStudents = enrollments.stream()
                .filter(e -> e.isAccepted() == Enrollment.EnrollmentStatus.ACCEPTED)
                .map(Enrollment::getStudent)
                .toList();

        model.addAttribute("subject", subject);
        model.addAttribute("subjectClass", subjectClass);
        model.addAttribute("sessionClass", subjectClass.getSessions());
        model.addAttribute("enrolledStudents", enrolledStudents);
        model.addAttribute("sideNavLinks", sideNavLinks);

        Map<Session, Attendance> classSessionMap = new LinkedHashMap<>();
        List<Session> classSessions = subjectClass.getSessions();

        return "teacher/class-details";  // Refers to src/main/resources/templates/user/dashboard.html
    }

    @GetMapping("/teacher/subjects/{subject_code}/{classId}/attendance/{aId}")
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

        return "teacher/attendance-details";
    }

    public Map<Session, List<Attendance>> getSessionsWithAcceptedAttendances(SubjectClass subjectClass) {
        // Fetch all sessions for the given subject class, sorted by week
        List<Session> sessions = sessionRepository.findBySubjectClassOrderByWeekAsc(subjectClass);

        // Initialize a map to hold sessions and their associated attendance records
        Map<Session, List<Attendance>> sessionAttendanceMap = new HashMap<>();

        // For each session, get the attendance records and filter by accepted enrollments
        for (Session session : sessions) {
            List<Attendance> attendances = session.getAttendanceRecords().stream()
                    .filter(att -> {
                        Enrollment enrollment = enrollmentRepository.findByStudentAndSubjectClass(att.getStudent(), subjectClass);
                        return enrollment != null && enrollment.isAccepted() == Enrollment.EnrollmentStatus.ACCEPTED;
                    })
                    .collect(Collectors.toList());

            sessionAttendanceMap.put(session, attendances);
        }

        return sessionAttendanceMap;
    }

    private Teacher getLoggedTeacher() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails)principal).getUsername();
            return teacherRepository.findByUsername(username);
        }

        return null;
    }

    private List<User> getEligibleReceivers(User user) {
        // Fetch students and admins associated with subjects the teacher is teaching
        List<Subject> subjects = subjectRepository.findByTeacher(user.getId());

        List<User> eligibleReceivers = new ArrayList<>();
        List<Enrollment> enrollments = new ArrayList<>();

        for (Subject subject : subjects) {
            enrollments.addAll(subject.getEnrollments()); // Assuming subjects track enrolled students
        }

        for (Enrollment enrollment : enrollments) {
            eligibleReceivers.add(enrollment.getStudent());
        }

        // Also fetch all admins (if needed)
        List<User> admins = userRepository.findByRole(Role.ADMIN);
        eligibleReceivers.addAll(admins);

        return eligibleReceivers;
    }


}

