package com.atom.challenge.dto.response;

public class ErrorResponse {
    private String[] messages;
    private int code;

    public ErrorResponse(int code, String... messages) {
        this.messages = messages;
        this.code = code;
    }

    public String[] getMessage() {
        return messages;
    }

    public int getCode() {
        return code;
    }

    public void setMessage(String[] messages) {
        this.messages = messages;
    }

    public void setCode(int code) {
        this.code = code;
    }
}