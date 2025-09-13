package com.cs203.core.dto;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class GenericResponse<E> {
    private HttpStatus status;
    private String message;
    private E data;

    public GenericResponse(HttpStatus status, String message, E data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
