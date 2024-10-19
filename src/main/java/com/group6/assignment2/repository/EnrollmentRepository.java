package com.group6.assignment2.repository;

import com.group6.assignment2.entity.Enrollment;
import com.group6.assignment2.entity.Student;
import com.group6.assignment2.entity.SubjectClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @Query("SELECT e FROM Enrollment e WHERE (e.student.id = :studentId) AND (e.subjectClass.id = :subjectClassId)")
    Enrollment findByStudentIdAndSubjectClassId(@Param("studentId") Long studentId, @Param("subjectClassId") Long subjectClassId);

    @Query("SELECT e FROM Enrollment e WHERE e.subjectClass.subject.code = :code AND e.isAccepted = 'ACCEPTED'")
    List<Enrollment> findEnrollmentsByStatusAccepted(@Param("code") String code);

    @Query("SELECT e FROM Enrollment e WHERE e.subjectClass.id = :id AND e.isAccepted = 'ACCEPTED'")
    List<Enrollment> findEnrollmentsByStatusAcceptedAndSubjectClass(@Param("id") Long id);

    Enrollment findByStudentAndSubjectClass(Student student, SubjectClass subjectClass);
}

