package com.group6.assignment2.repository;

import com.group6.assignment2.entity.Parent;
import com.group6.assignment2.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    @Query("SELECT s FROM Session s WHERE s.id = :id")
    Session findBySessionId(String id);
}
