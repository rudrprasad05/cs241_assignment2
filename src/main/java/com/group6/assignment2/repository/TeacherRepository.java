package com.group6.assignment2.repository;

import com.group6.assignment2.entity.Student;
import com.group6.assignment2.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query("SELECT t FROM Teacher t WHERE t.teacherId = :teacherId")
    Teacher findByTeacherId(@Param("teacherId") String teacherId);

    @Query("SELECT t FROM Teacher t WHERE t.id = :id")
    Teacher findTeacherById(@Param("id") Long id);

    Teacher findByUsername(String username);
}
