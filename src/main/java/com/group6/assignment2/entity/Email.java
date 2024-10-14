package com.group6.assignment2.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String header;
    private String body;
    private String subject;
    private String sender;
    private String receiver;

    public Email(String header, String body, String subject, String receiver) {
        this.header = header;
        this.body = body;
        this.subject = subject;
        this.sender = "no-reply@goshawkfiji.com";
        this.receiver = receiver;
    }

    public Email() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String to) {
        this.receiver = to;
    }
}
