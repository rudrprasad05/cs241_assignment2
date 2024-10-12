package com.group6.assignment2.repository;

import com.group6.assignment2.entity.InviteLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteLinkRepository extends JpaRepository<InviteLink, Long> {

    // Find InviteLink by its inviteCode
    InviteLink findByInviteCode(String inviteCode);
}
