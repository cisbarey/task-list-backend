package com.atom.challenge.domain;

import com.google.cloud.Timestamp;

public class Task {

    private String id;
    private String title;
    private String description;
    private Timestamp createdAt;
    private boolean completed;
    private String userId;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getUserId() {
        return userId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}