package com.group6.assignment2.repository;

import com.group6.assignment2.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @Query("SELECT a FROM Attendance a WHERE (a.id = :id)")
    Attendance findByAttendanceId(@Param("id") String studentId);

    Attendance findByStudentIdAndSessionId(Long id, Long id1);

    List<Attendance> findByStudentAndSession(Student student, Session session);

    @Modifying
    @Query("DELETE FROM Attendance a WHERE a.student = :student AND a.session IN :sessions")
    void deleteByStudentAndSessions(@Param("student") Student student, @Param("sessions") List<Session> sessions);

}

