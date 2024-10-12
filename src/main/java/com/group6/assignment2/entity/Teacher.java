package com.group6.assignment2.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Teacher extends User {

    @Column(nullable = false)
    private String teacherId;

    private String subjectSpecialization;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Subject> subjects;

    @OneToOne(mappedBy = "teacher", cascade = CascadeType.ALL)
    private InviteLink inviteLink;

    public Teacher() {}

    public Teacher(String username, String email, String password, String fName, String lName) {
        super(username, email, password, fName, lName);
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
    public InviteLink getInviteLink() {
        return inviteLink;
    }

    public void setInviteLink(InviteLink inviteLink) {
        this.inviteLink = inviteLink;
    }
}
