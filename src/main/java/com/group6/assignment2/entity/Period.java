package com.group6.assignment2.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Period {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "period", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubjectClass> subjectClasses;

    private LocalTime time;

    public Period() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<SubjectClass> getSubjectClasses() {
        return subjectClasses;
    }

    public void setSubjectClasses(List<SubjectClass> subjectClasses) {
        this.subjectClasses = subjectClasses;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Period(LocalTime time) {
        this.time = time;
    }
}
