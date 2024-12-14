package com.ps.cinema.server.service;

import com.ps.cinema.server.BasicResponce.BasicMessageResponse;
import com.ps.cinema.server.dto.CinemaUserDto;
import com.ps.cinema.server.model.User;
import org.springframework.http.ResponseEntity;

public interface CinemaAppUserService {
    public ResponseEntity<BasicMessageResponse> getCinemaUserByNameR(String name);

    public ResponseEntity<BasicMessageResponse> getCinemaUserByEmailR(String email);

    public ResponseEntity<BasicMessageResponse> getCinemaUserByIdR(int id);

    public User getCinemaUserByUsername(String name);

    public boolean isUserExist(String username);
    public User getCinemaUserById(int id);
    public User getCinemaUserByEmail(String email);

}
