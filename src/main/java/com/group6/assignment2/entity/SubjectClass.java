package com.group6.assignment2.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SubjectClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String day;  // Updated to LocalDateTime

    @ManyToOne
    @JoinColumn(name = "period_id", nullable = false)
    private Period period;
    private String roomCode;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @OneToMany(mappedBy = "subjectClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments;

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    @OneToMany(mappedBy = "subjectClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions;

    public SubjectClass(String day, Period time, String roomCode, Subject subject) {
        this.day = day;
        this.period = time;
        this.roomCode = roomCode;
        this.subject = subject;

        int weeks = 13;
        List<Session> sessionList = new ArrayList<>();

        for(int i = 1; i <= weeks; i++) {
            Session tempSession = new Session(i, this);
            sessionList.add(tempSession);
        }

        this.sessions = sessionList;
    }

    public SubjectClass() {}

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
