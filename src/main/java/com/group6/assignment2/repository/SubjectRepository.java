package com.group6.assignment2.repository;

import com.group6.assignment2.entity.Subject;
import com.group6.assignment2.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    @Query("SELECT s FROM Subject s WHERE s.teacher IS NULL")
    List<Subject> findSubjectsWithoutTeacher();

    Page<Subject> findAll(Pageable pageable);

//    @Query("SELECT s FROM Subject s WHERE s.subjectName = :name")
    Subject findByName(@Param("name") String name);

    @Query("SELECT s FROM Subject s WHERE s.code = :code")
    Subject findByCode(@Param("code") String code);

    @Query(value = "SELECT * FROM subject WHERE teacher_id = :teacherId", nativeQuery = true)
    List<Subject> findByTeacher(@Param("teacherId") Long teacherId);


}
