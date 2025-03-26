package com.atom.challenge.exception;

public class DataNotFoundException extends RuntimeException {

    private String message;

    public DataNotFoundException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
