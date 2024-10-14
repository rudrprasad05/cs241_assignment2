package com.group6.assignment2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")

public class Admin extends User {
    private String adminLevel;

    public String getAdminLevel() {
        return adminLevel;
    }

    public Admin(){}

    public Admin(String username, String fName, String lName, String email, String personalEmail, String password) {
        super(username, email, password, fName, lName, Role.ADMIN);
        this.setPersonalEmail(personalEmail);

    }

    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }
}
