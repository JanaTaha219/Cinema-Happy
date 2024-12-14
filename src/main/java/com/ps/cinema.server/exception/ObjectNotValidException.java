package com.ps.cinema.server.exception;

import org.springframework.http.HttpStatus;

import java.util.Set;

public class ObjectNotValidException extends RuntimeException {
    private Set<String> message;
    private HttpStatus httpStatus;

    public ObjectNotValidException(Set<String> errorMessages) {
        this.message = errorMessages;
        this.httpStatus=HttpStatus.BAD_REQUEST;
    }



    @Override
    public String toString() {
        return "ObjectNotValidException{" +
                "errorMessages=" + message +
                '}';
    }


}
