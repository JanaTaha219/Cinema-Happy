package com.ps.cinema.server.exception;

import lombok.*;
import org.springframework.http.HttpStatus;


@Getter @Setter
@ToString
public class ObjectNotFoundException extends RuntimeException {
    private String message;
    private HttpStatus httpStatus;

    public ObjectNotFoundException(){
        this.message = "Object is not found in the database!";
        this.httpStatus = HttpStatus.NOT_FOUND;
    }
    public ObjectNotFoundException(String message){
        this.message = message;
        this.httpStatus = HttpStatus.NOT_FOUND;
    }

    public ObjectNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

}