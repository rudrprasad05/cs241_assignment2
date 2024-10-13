package com.group6.assignment2.repository;

import com.group6.assignment2.entity.Subject;
import com.group6.assignment2.entity.SubjectClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectClassRepository extends JpaRepository<SubjectClass, Long> {
    @Query("SELECT s FROM SubjectClass s WHERE s.id IS NULL")
    List<Subject> findSubjectsWithoutTeacher();


    @Query("SELECT s FROM SubjectClass s WHERE s.id = :code")
    SubjectClass findByCode(@Param("code") String code);

    @Query("SELECT s FROM SubjectClass s WHERE s.subject.code = :code")
    List<SubjectClass> findBySubjectCode(@Param("code") String code);

}
