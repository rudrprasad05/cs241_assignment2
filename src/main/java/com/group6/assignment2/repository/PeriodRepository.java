package com.group6.assignment2.repository;

import com.group6.assignment2.entity.InviteLink;
import com.group6.assignment2.entity.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Long> {

    // Find InviteLink by its inviteCode
}
