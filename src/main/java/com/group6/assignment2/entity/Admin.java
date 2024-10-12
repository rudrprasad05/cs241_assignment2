package com.group6.assignment2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Admin extends User {
    private String adminLevel;

    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }
}
