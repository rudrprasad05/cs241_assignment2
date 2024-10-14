package com.group6.assignment2.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {
    @Column(nullable = false, unique = true)
    private String studentId;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendanceRecords;

    public Student(String username, String fName, String lName, String email, String password, String studentId) {
        super(username, email, password, fName, lName, Role.STUDENT);
        this.studentId = studentId;

    }
    public Student() {
        super();
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public List<Attendance> getAttendanceRecords() {
        return attendanceRecords;
    }

    public void setAttendanceRecords(List<Attendance> attendanceRecords) {
        this.attendanceRecords = attendanceRecords;
    }

}
