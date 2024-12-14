package com.ps.cinema.server.service;

import com.ps.cinema.server.BasicResponce.BasicMessageResponse;
import com.ps.cinema.server.dto.*;
import com.ps.cinema.server.exception.GeneralException;
import com.ps.cinema.server.mapper.EntityDtoConverter;
import com.ps.cinema.server.model.*;
import com.ps.cinema.server.repository.*;
import com.ps.cinema.server.validator.ObjectsValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CinemaAppUserServiceImpl implements CinemaAppUserService {

    @Autowired
    EntityDtoConverter converter;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProducerRatingRepository producerRatingRepository;

    @Autowired
    ObjectsValidator<User> cinemaAppUserObjectsValidator;

    public User getCinemaUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new GeneralException("User is not found in the database.", HttpStatus.NOT_FOUND));
    }

    public User getCinemaUserByUsername(String name) {
        return userRepository.findCinemaAppUserByUsername(name).orElseThrow(() -> new GeneralException("User is not found in the database.", HttpStatus.NOT_FOUND));
    }

    public User getCinemaUserByEmail(String email) {
        return userRepository.findCinemaAppUserByEmail(email).orElseThrow(() -> new GeneralException("User is not found in the database.", HttpStatus.NOT_FOUND));
    }

    public boolean isUserExist(String username) {
        try {
            getCinemaUserByNameR(username);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ResponseEntity<BasicMessageResponse> getCinemaUserByNameR(String name) {
        CinemaUserDtoG cinemaUserDtoG = converter.convertToDto(userRepository
                .findCinemaAppUserByUsername(name)
                .orElseThrow(() ->
                        new GeneralException("User is not found in the database.", HttpStatus.NOT_FOUND)), CinemaUserDtoG.class);
        return new ResponseEntity<>
                (new BasicMessageResponse("User", cinemaUserDtoG, 200), HttpStatus.OK);
    }

    public ResponseEntity<BasicMessageResponse> getCinemaUserByEmailR(String email) {
        CinemaUserDtoG cinemaUserDtoG = converter.convertToDto(userRepository
                .findCinemaAppUserByEmail(email)
                .orElseThrow(() ->
                        new GeneralException("User is not found in the database.", HttpStatus.NOT_FOUND)), CinemaUserDtoG.class);
        return new ResponseEntity<>
                (new BasicMessageResponse("User", cinemaUserDtoG, 200), HttpStatus.OK);
    }

    public ResponseEntity<BasicMessageResponse> getCinemaUserByIdR(int id) {
        CinemaUserDtoG cinemaUserDtoG = converter.convertToDto(userRepository
                .findById(id)
                .orElseThrow(() ->
                        new GeneralException("User is not found in the database.", HttpStatus.NOT_FOUND)), CinemaUserDtoG.class);
        return new ResponseEntity<>
                (new BasicMessageResponse("User", cinemaUserDtoG, 200), HttpStatus.OK);
    }
}