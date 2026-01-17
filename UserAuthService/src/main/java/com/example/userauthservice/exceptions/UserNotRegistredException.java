package com.example.userauthservice.exceptions;

public class UserNotRegistredException extends RuntimeException {
    public UserNotRegistredException(String message) {
        super(message);
    }
}
