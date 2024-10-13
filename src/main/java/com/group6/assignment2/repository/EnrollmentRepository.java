package com.group6.assignment2.repository;

import com.group6.assignment2.entity.Enrollment;
import com.group6.assignment2.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @Query("SELECT e FROM Enrollment e WHERE (e.student.id = :studentId) AND (e.subjectClass.id = :subjectClassId)")
    Enrollment findByStudentIdAndSubjectClassId(@Param("studentId") Long studentId, @Param("subjectClassId") Long subjectClassId);

}
