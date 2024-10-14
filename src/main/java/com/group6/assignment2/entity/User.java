package com.group6.assignment2.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Use Joined strategy for inheritance
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, unique = true)
    protected String email;

    @Column(nullable = false, unique = true)
    protected String username;

    @Column(nullable = false)
    protected String password;

    protected String fName;
    protected String lName;
    protected String profileImage;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<Notification> receivedNotifications;

    public List<Notification> getSentNotifications() {
        return sentNotifications;
    }

    public void setSentNotifications(List<Notification> sentNotifications) {
        this.sentNotifications = sentNotifications;
    }

    public List<Notification> getReceivedNotifications() {
        return receivedNotifications;
    }

    public void setReceivedNotifications(List<Notification> receivedNotifications) {
        this.receivedNotifications = receivedNotifications;
    }

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<Notification> sentNotifications;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User(){};

    public User(String username, String email, String password, String fName, String lName, Role role) {
        this.email = email;
        this.password = password;
        this.fName = fName;
        this.lName = lName;
        this.username = username;
        this.role = role;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
