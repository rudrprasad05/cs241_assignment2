package com.group6.assignment2.controllers;

import com.group6.assignment2.entity.Email;
import com.resend.Resend;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import org.springframework.stereotype.Controller;

@Controller
public class EmailController {
    public static void SendAutomatedEmail(Email email) {
        Resend resend = new Resend("re_L12baCqE_9HddikikXYrKWXw4QAwf6ssA");

        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from(email.getSender())
                .to(email.getReceiver())
                .subject(email.getSubject())
                .html(email.getBody())
                .build();

        SendEmailResponse data = resend.emails().send(sendEmailRequest);
    }
}
