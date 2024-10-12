package com.group6.assignment2.entity;

import jakarta.persistence.*;

@Entity
public class Parent extends User {
    @Column(nullable = false)
    private String parentId;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}