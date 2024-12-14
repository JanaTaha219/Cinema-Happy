package com.ps.cinema.server.exception;

import org.springframework.http.HttpStatus;

public class UserNotAuthorizedException extends RuntimeException{
    private String message;
    private HttpStatus httpStatus;
    public UserNotAuthorizedException(){
        this.message = "User is not Authorized!";
        this.httpStatus = HttpStatus.UNAUTHORIZED;
    }
    public UserNotAuthorizedException(String message){
        this.message = message;
        this.httpStatus = HttpStatus.UNAUTHORIZED;
    }

    public UserNotAuthorizedException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
