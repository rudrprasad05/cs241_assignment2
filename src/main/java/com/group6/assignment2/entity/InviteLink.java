package com.group6.assignment2.entity;

import jakarta.persistence.*;

@Entity
public class InviteLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String inviteCode;

    public InviteLink() {
    }

    private boolean isRedeemed;

    // One-to-one relationship with Teacher
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public InviteLink(String inviteCode) {
        this.inviteCode = inviteCode;
        this.isRedeemed = false;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public boolean isRedeemed() {
        return isRedeemed;
    }

    public void setRedeemed(boolean redeemed) {
        isRedeemed = redeemed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
