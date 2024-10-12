package com.group6.assignment2.entity;

import com.group6.assignment2.repository.StudentRepository;
import jakarta.persistence.*;

import java.util.Random;

@Entity
public class Student extends User {
    @Column(nullable = false, unique = true)
    private String studentId;

    public Student(String username, String fName, String lName, String email, String password, String studentId) {
        super(username, email, password, fName, lName); // Assuming User class has a fullName field
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


}
