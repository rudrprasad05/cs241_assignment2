
package com.group6.assignment2.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
public class Enrollment {

    public enum EnrollmentStatus {
        PENDING, ACCEPTED, REJECTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus isAccepted;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "subject_class_id", nullable = false)  // Foreign key to SubjectClass
    private SubjectClass subjectClass;

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public EnrollmentStatus getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(EnrollmentStatus isAccepted) {
        this.isAccepted = isAccepted;
    }

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)  // Foreign key to SubjectClass
    private Subject subject;

    public Enrollment() {}
    public  Enrollment(Student student, SubjectClass subjectClass, Subject subject) {
        this.createdAt = LocalDateTime.now();
        this.isAccepted = EnrollmentStatus.PENDING;
        this.student = student;
        this.subjectClass = subjectClass;
        this.subject = subject;
    }

    public  Enrollment(Student student, SubjectClass subjectClass, Subject subject, EnrollmentStatus isAccepted) {
        this.createdAt = LocalDateTime.now();
        this.isAccepted = isAccepted;
        this.student = student;
        this.subjectClass = subjectClass;
        this.subject = subject;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public EnrollmentStatus isAccepted() {
        return isAccepted;
    }

    public void setAccepted(EnrollmentStatus accepted) {
        isAccepted = accepted;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public SubjectClass getSubjectClass() {
        return subjectClass;
    }

    public void setSubjectClass(SubjectClass subjectClass) {
        this.subjectClass = subjectClass;
    }
}
