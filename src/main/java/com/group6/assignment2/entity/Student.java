package com.group6.assignment2.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {
    public Student(String username, String fName, String lName, String email, String personalEmail, String password) {
        super(username, email, password, fName, lName, Role.STUDENT);
        this.setPersonalEmail(personalEmail);
        this.studentId = username;

    }
    public Student() {
        super();
    }

    @Column(nullable = false, unique = true)
    private String studentId;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parent> parents;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendanceRecords;

    public List<Parent> getParents() {
        return parents;
    }

    public void setParents(List<Parent> parents) {
        this.parents = parents;
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
