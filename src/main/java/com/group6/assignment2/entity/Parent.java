package com.group6.assignment2.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("PARENT")

public class Parent extends User {
    @Column(nullable = false)
    private String parentId;

    public String getParentId() {
        return parentId;
    }
    public Parent(String username, String fName, String lName, String email, String personalEmail, String password) {
        super(username, email, password, fName, lName, Role.PARENT);
        this.setPersonalEmail(personalEmail);
        this.parentId = username;

    }
    public Parent() {}

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    public Student getStudent() {
        return student;
    }
    public void setStudent(Student student) {
        this.student = student;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}