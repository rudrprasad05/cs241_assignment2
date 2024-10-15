package com.group6.assignment2.repository;

import com.group6.assignment2.entity.Parent;
import com.group6.assignment2.entity.Session;
import com.group6.assignment2.entity.SubjectClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    @Query("SELECT s FROM Session s WHERE s.id = :id")
    Session findBySessionId(String id);

    List<Session> findBySubjectClassOrderByWeekAsc(SubjectClass subjectClass);

    @Query("SELECT s FROM Session s " +
            "JOIN s.attendanceRecords a " +
            "JOIN a.student st " +
            "JOIN st.enrollments e " +
            "WHERE e.subjectClass = :subjectClass " +
            "AND e.isAccepted = 'ACCEPTED' " +
            "ORDER BY s.week ASC")
    List<Session> findSessionsWithAcceptedAttendances(@Param("subjectClass") SubjectClass subjectClass);
}
