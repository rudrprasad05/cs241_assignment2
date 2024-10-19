package com.group6.assignment2.repository;

import com.group6.assignment2.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT s FROM Student s WHERE s.studentId = :studentId")
    Student findByStudentId(@Param("studentId") String studentId);

    @Query("SELECT s FROM Student s WHERE s.email = :email")
    Student findByEmail(@Param("email") String email);

    @Query("SELECT s FROM Student s WHERE s.username = :username")
    Student findByUsername(@Param("username") String username);

    @Query("SELECT s FROM Student s WHERE s.id NOT IN (SELECT e.student.id FROM Enrollment e WHERE e.subject.id = :subjectId)")
    List<Student> findStudentsNotEnrolledInSubject(@Param("subjectId") Long subjectId);

}
