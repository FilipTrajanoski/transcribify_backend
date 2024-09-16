package com.example.transcribify.model.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String id) {
        super(String.format("User with %s does not exist", id));
    }
}
