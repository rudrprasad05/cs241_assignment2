package com.group6.assignment2.repository;

import com.group6.assignment2.entity.Email;
import com.group6.assignment2.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

}
