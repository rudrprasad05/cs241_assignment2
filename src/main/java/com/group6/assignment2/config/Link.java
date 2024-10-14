package com.group6.assignment2.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Link {
    private String url;
    private String label;

    public Link(String url, String label) {
        this.url = url;
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public String getLabel() {
        return label;
    }

    public static List<Link> addLinks(String type){
        List<Link> links = new ArrayList<>();

        if(Objects.equals(type, "student")){
            links.add(new Link("/student/dashboard", "Dashboard"));
            links.add(new Link("/student/subjects", "Subjects"));
        }
        else if(Objects.equals(type, "teacher")){
            links.add(new Link("/teacher/dashboard", "Subjects"));
            links.add(new Link("/teacher/notifications", "Notifications"));
            links.add(new Link("/teacher/invite-teacher", "Invite Teacher"));
        }
        else if(Objects.equals(type, "admin")){
            links.add(new Link("/admin/subjects", "Subjects"));
            links.add(new Link("/admin/invite-teacher", "Invite Teacher"));
        }

        return links;
    }
}

