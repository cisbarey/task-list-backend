package com.atom.challenge.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UserRequest {

    @NotBlank(message = "Username is mandatory")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
