package com.group6.assignment2.repository;

import com.group6.assignment2.entity.Parent;
import com.group6.assignment2.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    @Query("SELECT s FROM Parent s WHERE s.username = :username")
    Optional<Parent> findByUsername(@Param("username") String username);
}
