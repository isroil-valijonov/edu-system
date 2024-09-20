package com.example.edusystem.exceptions;

public class CustomException extends Exception {

    public CustomException(String massage){
        super(massage);
    }

    public CustomException(String massage, int status){
        super(String.format(massage, status));
    }

}
