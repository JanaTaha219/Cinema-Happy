package com.ps.cinema.server.handler;

import com.ps.cinema.server.BasicResponce.BasicMessageResponse;
import com.ps.cinema.server.exception.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler({GeneralException.class})
    public ResponseEntity<BasicMessageResponse> handleGeneralException(GeneralException exp){
        Map x= new HashMap();
        x.put("Error", exp.getMessage());
        return ResponseEntity.badRequest().body(
                new BasicMessageResponse(x, exp.getHttpStatus().value()));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<BasicMessageResponse> handleException(Exception exp){
        return ResponseEntity.badRequest().body(
                new BasicMessageResponse("Error", exp.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}