package com.ps.cinema.server.service;

import com.ps.cinema.server.BasicResponce.BasicMessageResponse;
import com.ps.cinema.server.dto.CinemaUserDto;
import com.ps.cinema.server.model.User;
import com.ps.cinema.server.model.Token;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    public ResponseEntity<BasicMessageResponse> authenticate(CinemaUserDto cinemaUserDto);

    public ResponseEntity<BasicMessageResponse> signUp(CinemaUserDto cinemaUserDto);

    public Token getTokenn(String token);

    public ResponseEntity<BasicMessageResponse> logout(String token);

    public void checkIfValidateToken(String token, User user);
}
