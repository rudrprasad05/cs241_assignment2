package com.group6.assignment2.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("TEACHER")

public class Teacher extends User {

    @Column(nullable = false)
    private String teacherId;

    private String subjectSpecialization;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Subject> subjects;

    public Teacher() {}

    public Teacher(String username, String email, String personalEmail, String password, String fName, String lName) {
        super(username, email, password, fName, lName, Role.TEACHER);
        this.setPersonalEmail(personalEmail);
        this.teacherId = username;
    }

    // Getters and Setters
    public String getSubjectSpecialization() {
        return subjectSpecialization;
    }

    public void setSubjectSpecialization(String subjectSpecialization) {
        this.subjectSpecialization = subjectSpecialization;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String id) {
        this.teacherId = id;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

}
