package com.cs203.core.exception;

public class InvalidUserCredentials extends RuntimeException{
    public InvalidUserCredentials(String message) {
        super(message);
    }
}
