package com.group6.assignment2.repository;

import com.group6.assignment2.entity.Notification;
import com.group6.assignment2.entity.Parent;
import com.group6.assignment2.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n WHERE n.receiver.id = :id")
    Notification findByReceiverId(@Param("id") Long id);
}
