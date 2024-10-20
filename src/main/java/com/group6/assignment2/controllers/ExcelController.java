package com.group6.assignment2.controllers;
import com.group6.assignment2.entity.*;
import com.group6.assignment2.repository.*;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

@Controller
public class ExcelController {

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private SubjectClassRepository subjectClassRepository;
    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(ExcelController.class);

    String time = LocalTime.now().toString();
    String filename = "report-" + time + ".xlsx";

    @PostMapping("/admin/generate-all-user-report")
    public String generateAllUserReports(
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request
    ) {

        List<User> users = userRepository.findAll();
        users.sort(Comparator.comparing(User::getId));

        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        try (Workbook workbook = new XSSFWorkbook(); ServletOutputStream outputStream = response.getOutputStream()) {
            Sheet attendanceSheet = workbook.createSheet("User Report");

            // Create header row
            Row headerRow = attendanceSheet.createRow(0);
            headerRow.createCell(0).setCellValue("Id");
            headerRow.createCell(1).setCellValue("Username");
            headerRow.createCell(2).setCellValue("Full Name");
            headerRow.createCell(3).setCellValue("Email");
            headerRow.createCell(4).setCellValue("Role");

            attendanceSheet.setColumnWidth(0, 1000);  // Student Name
            attendanceSheet.setColumnWidth(1, 5000);  // Student Name
            attendanceSheet.setColumnWidth(2, 10000);  // Email
            attendanceSheet.setColumnWidth(3, 10000);  // Attendance Date
            attendanceSheet.setColumnWidth(4, 4000);  // Status

            // Iterate over each attendance record and print the information
            int rowNum = 1;
            for (User user : users) {
                // Fetch associated student details

                if (user != null) {
                    Row row = attendanceSheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(user.getId());
                    row.createCell(1).setCellValue(user.getUsername());
                    row.createCell(2).setCellValue(user.getFName() + " " + user.getLName());
                    row.createCell(3).setCellValue(user.getEmail());
                    row.createCell(4).setCellValue(user.getRole().toString());
                }
            }
            // Populate your sheet here with data
            workbook.write(outputStream);
            workbook.write(outputStream);
            workbook.close();
            outputStream.flush();

        } catch (IOException e) {
            logger.error("Failed to generate Excel file", e);
            String referer = request.getHeader("Referer");
            redirectAttributes.addFlashAttribute("toastMessage", "Report could not be downloaded");
            redirectAttributes.addFlashAttribute("toastType", "fail");  // You can send 'success', 'error', etc.

            return "redirect:" + referer;
        }

        redirectAttributes.addFlashAttribute("toastMessage", "Report downloaded Successfully");
        redirectAttributes.addFlashAttribute("toastType", "success");  // You can send 'success', 'error', etc.

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PostMapping("/admin/generate-report")
    public String generateExcel(
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            @RequestParam("subjectCode") String subjectCode
    ) {

        Subject subject = subjectRepository.findByCode(subjectCode);
        Teacher teacher = subject != null ? teacherRepository.findById(subject.getTeacher().getId()).orElse(new Teacher()) : null;

        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        try (Workbook workbook = new XSSFWorkbook(); ServletOutputStream outputStream = response.getOutputStream()) {
            Sheet subjectSheet = workbook.createSheet("Subject Details");
            Sheet teacherSheet = workbook.createSheet("Teacher Details");

            assert subject != null;
            fillSubjectDetails(subjectSheet, subject);
            assert teacher != null;
            fillTeacherDetails(teacherSheet, teacher);
            // Populate your sheet here with data
            workbook.write(outputStream);
            workbook.write(outputStream);
            workbook.close();
            outputStream.flush();

        } catch (IOException e) {
            logger.error("Failed to generate Excel file", e);
            String referer = request.getHeader("Referer");
            redirectAttributes.addFlashAttribute("toastMessage", "Report could not be downloaded");
            redirectAttributes.addFlashAttribute("toastType", "fail");  // You can send 'success', 'error', etc.

            return "redirect:" + referer;
        }

        redirectAttributes.addFlashAttribute("toastMessage", "Report downloaded Successfully");
        redirectAttributes.addFlashAttribute("toastType", "success");  // You can send 'success', 'error', etc.

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PostMapping("/admin/generate-weekly-report")
    public String generateWeeklyReport(
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            @RequestParam("subjectCode") String subjectCode,
            @RequestParam("sessionId") Long sessionId
    ) {

        Subject subject = subjectRepository.findByCode(subjectCode);
        Session session = sessionRepository.findById(sessionId).orElse(null);

        if(session == null || subject == null) {
            String referer = request.getHeader("Referer");
            redirectAttributes.addFlashAttribute("toastMessage", "Report could not be downloaded");
            redirectAttributes.addFlashAttribute("toastType", "fail");
            return "redirect:" + referer;
        }

        List<Attendance> attendances = session.getAttendanceRecords();


        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        try (Workbook workbook = new XSSFWorkbook(); ServletOutputStream outputStream = response.getOutputStream()) {
            Sheet subjectSheet = workbook.createSheet("Attendance Details");

            Sheet attendanceSheet = workbook.createSheet("Attendance Report");

            // Create header row
            Row headerRow = attendanceSheet.createRow(0);
            headerRow.createCell(0).setCellValue("Student Name");
            headerRow.createCell(1).setCellValue("Email");
            headerRow.createCell(2).setCellValue("Attendance Date");
            headerRow.createCell(3).setCellValue("Status");

            attendanceSheet.setColumnWidth(0, 5000);  // Student Name
            attendanceSheet.setColumnWidth(1, 8000);  // Email
            attendanceSheet.setColumnWidth(2, 5000);  // Attendance Date
            attendanceSheet.setColumnWidth(3, 4000);  // Status

            // Iterate over each attendance record and print the information
            int rowNum = 1;
            for (Attendance attendance : attendances) {
                // Fetch associated student details
                Student student = attendance.getStudent();

                if (student != null) {
                    Row row = attendanceSheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(student.getFName());
                    row.createCell(1).setCellValue(student.getEmail());
                    row.createCell(3).setCellValue(attendance.isPresent().toString());
                }
            }
            // Populate your sheet here with data
            workbook.write(outputStream);
            workbook.write(outputStream);
            workbook.close();
            outputStream.flush();

        } catch (IOException e) {
            logger.error("Failed to generate Excel file", e);
            String referer = request.getHeader("Referer");
            redirectAttributes.addFlashAttribute("toastMessage", "Report could not be downloaded");
            redirectAttributes.addFlashAttribute("toastType", "fail");  // You can send 'success', 'error', etc.

            return "redirect:" + referer;
        }

        redirectAttributes.addFlashAttribute("toastMessage", "Report downloaded Successfully");
        redirectAttributes.addFlashAttribute("toastType", "success");  // You can send 'success', 'error', etc.

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }


    @PostMapping("/redirect/generate-class-report")
    public String generateClassReportForEveryWeek(
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            @RequestParam("subjectCode") String subjectCode,
            @RequestParam("subjectClassId") Long subjectClassId
    ) throws IOException {

        Subject subject = subjectRepository.findByCode(subjectCode);
        SubjectClass subjectClass = subjectClassRepository.findById(subjectClassId).orElse(null);

        if(subjectClass == null || subject == null) {
            String referer = request.getHeader("Referer");
            redirectAttributes.addFlashAttribute("toastMessage", "Report could not be downloaded");
            redirectAttributes.addFlashAttribute("toastType", "fail");
            return "redirect:" + referer;
        }

        List<Session> sessions = subjectClass.getSessions();
        sessions.sort(Comparator.comparing(Session::getWeek));

        Map<Session, List<Attendance>> sessionAttendanceMap = new HashMap<>();

        for(Session s : sessions){
            List<Attendance> a = s.getAttendanceRecords();
            sessionAttendanceMap.put(s, a);
        }


        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");



        try (Workbook workbook = new XSSFWorkbook(); ServletOutputStream outputStream = response.getOutputStream()) {
            // Create a sheet for attendance
            String filename = "AttendanceReport.xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            Map<String, List<Attendance>> allAttendancesMappedByStatus = new HashMap<>();

            allAttendancesMappedByStatus.put("PRESENT", new ArrayList<>());
            allAttendancesMappedByStatus.put("ABSENT", new ArrayList<>());
            allAttendancesMappedByStatus.put("LATE", new ArrayList<>());
            allAttendancesMappedByStatus.put("NOT_MARKED", new ArrayList<>());
            allAttendancesMappedByStatus.put("EXCUSED", new ArrayList<>());


            for (Session session : sessions) {
                Sheet attendanceSheet = workbook.createSheet("Attendance for Week " + session.getWeek());

                // Create header row
                Row headerRow = attendanceSheet.createRow(0);
                headerRow.createCell(0).setCellValue("Student Name");
                headerRow.createCell(1).setCellValue("Email");
                headerRow.createCell(2).setCellValue("Excuse");
                headerRow.createCell(3).setCellValue("Status");

                // Set column widths
                attendanceSheet.setColumnWidth(0, 5000);  // Student Name
                attendanceSheet.setColumnWidth(1, 8000);  // Email
                attendanceSheet.setColumnWidth(2, 5000);  // Attendance Date
                attendanceSheet.setColumnWidth(3, 4000);  // Status

                // Fetch attendance for the current session
                List<Attendance> attendances = sessionAttendanceMap.get(session);

                // Populate the sheet with attendance data
                int rowNum = 1;
                for (Attendance attendance : attendances) {
                    Student student = attendance.getStudent();
                    if (student != null) {
                        String status = attendance.isPresent().toString();
                        Row row = attendanceSheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(student.getFName());  // Assuming getFName() gets the first name
                        row.createCell(1).setCellValue(student.getEmail());
                        row.createCell(2).setCellValue(attendance.getExcuse());  // Assuming attendance has a getDate()
                        row.createCell(3).setCellValue(attendance.isPresent().toString());

                        List<Attendance> a = allAttendancesMappedByStatus.get(status);
                        a.add(attendance);
                        allAttendancesMappedByStatus.put(status, a);

                    }
                }
            }
            int totalAttendances = 0;
            int numberPresent = allAttendancesMappedByStatus.get("PRESENT").size();
            int numberAbsent = allAttendancesMappedByStatus.get("ABSENT").size();
            int numberExcused = allAttendancesMappedByStatus.get("EXCUSED").size();
            int numberLate = allAttendancesMappedByStatus.get("LATE").size();
            int numberNotMarked = allAttendancesMappedByStatus.get("NOT_MARKED").size();

            logger.info(String.valueOf(numberPresent), numberAbsent, numberExcused, numberLate, numberNotMarked);


            for (Map.Entry<String, List<Attendance>> entry : allAttendancesMappedByStatus.entrySet()) {
                List<Attendance> attendances = entry.getValue();
                totalAttendances += attendances.size();  // Add the size of the current list to the total
            }

            Sheet summarySheet = workbook.createSheet("Summary");

            summarySheet.setColumnWidth(0, 5000);
            summarySheet.setColumnWidth(1, 5000);
            summarySheet.setColumnWidth(2, 5000);

            Row firstRow = summarySheet.createRow(0);
            firstRow.createCell(0).setCellValue("Total Students");
            firstRow.createCell(1).setCellValue(totalAttendances);

            Row secondRow = summarySheet.createRow(1);
            secondRow.createCell(0).setCellValue("Total Present");
            secondRow.createCell(1).setCellValue(numberPresent);
            secondRow.createCell(2).setCellValue(((double) numberPresent * 100 / totalAttendances) + "%");

            Row thirdRow = summarySheet.createRow(2);
            thirdRow.createCell(0).setCellValue("Total Absent");
            thirdRow.createCell(1).setCellValue(numberPresent);
            thirdRow.createCell(2).setCellValue(((double) numberAbsent * 100 / totalAttendances) + "%");

            Row fourth = summarySheet.createRow(3);
            fourth.createCell(0).setCellValue("Total Unmarked");
            fourth.createCell(1).setCellValue(numberPresent);
            fourth.createCell(2).setCellValue(((double) numberNotMarked * 100 / totalAttendances) + "%");

            Row fifth = summarySheet.createRow(4);
            fifth.createCell(0).setCellValue("Total Late");
            fifth.createCell(1).setCellValue(numberLate);
            fifth.createCell(2).setCellValue(((double) numberLate * 100 / totalAttendances) + "%");

            Row sixth = summarySheet.createRow(4);
            sixth.createCell(0).setCellValue("Total Excused");
            sixth.createCell(1).setCellValue(numberExcused);
            sixth.createCell(2).setCellValue(((double) numberExcused * 100 / totalAttendances) + "%");

            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Failed to generate report for session: ", e);
        }


        redirectAttributes.addFlashAttribute("toastMessage", "Report downloaded Successfully");
        redirectAttributes.addFlashAttribute("toastType", "success");  // You can send 'success', 'error', etc.

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    private void fillSubjectDetails(Sheet sheet, Subject subject) {
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Subject Name");
        row.createCell(1).setCellValue(subject.getName());

        row = sheet.createRow(1);
        row.createCell(0).setCellValue("Description");
        row.createCell(1).setCellValue(subject.getDescription());
        // Add more details as needed
    }



    private void fillTeacherDetails(Sheet sheet, Teacher teacher) {
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Teacher Name");
        row.createCell(1).setCellValue(teacher.getFName());
        // Add more details as needed
    }
}