package com.group6.assignment2.repository;

import com.group6.assignment2.entity.Attendance;
import com.group6.assignment2.entity.Enrollment;
import com.group6.assignment2.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @Query("SELECT a FROM Attendance a WHERE (a.id = :id)")
    Attendance findByAttendanceId(@Param("id") String studentId);

}
