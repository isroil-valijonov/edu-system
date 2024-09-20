package com.example.edusystem.exceptions;


public class CustomNotFoundException extends Exception{
    public CustomNotFoundException(String message) {
        super(message);
    }
    public CustomNotFoundException (String message, int status) {
        super(String.format(message, status));
    }
}
