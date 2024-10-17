package com.group6.assignment2.repository;

import com.group6.assignment2.entity.Admin;
import com.group6.assignment2.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Query("SELECT s FROM Admin s WHERE s.username = :username")
    Admin findByUsername(String username);
}
