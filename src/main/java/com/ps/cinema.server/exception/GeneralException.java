package com.ps.cinema.server.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter  @Setter
@ToString
public class GeneralException extends RuntimeException{
    private HttpStatus httpStatus;
    private Exception exception;

    public GeneralException(){
        super("An error happened");
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    public GeneralException(String message){
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public GeneralException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public GeneralException(String message, HttpStatus httpStatus, Exception exception) {
        super(message);
        this.httpStatus = httpStatus;
        this.exception = exception;
    }
}
