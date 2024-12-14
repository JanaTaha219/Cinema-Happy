package com.ps.cinema.server.service;

import com.ps.cinema.server.BasicResponce.BasicMessageResponse;
import com.ps.cinema.server.dto.CinemaUserDtoG;
import com.ps.cinema.server.dto.FilmDto;
import com.ps.cinema.server.dto.ProducerDto;
import com.ps.cinema.server.exception.GeneralException;
import com.ps.cinema.server.mapper.EntityDtoConverter;
import com.ps.cinema.server.model.*;
import com.ps.cinema.server.repository.ProducerRatingRepository;
import com.ps.cinema.server.repository.UserRepository;
import com.ps.cinema.server.validator.ObjectsValidator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProducerServiceImpl implements ProducerService{

    @Autowired
    FilmService filmService;

    @Autowired
    EntityDtoConverter converter;

    @Autowired
    ObjectsValidator<Film> filmsValidator;

    @Autowired
    ProducerRatingRepository producerRatingRepository;

    @Autowired
    AdminService adminService;

    @Autowired
    UserRepository userRepository;

    private boolean validateUser(String id, SecurityUser securityUser, String idType) {
        User producer;
        boolean isAdmin=false;
        try {
            producer = switch (idType) {
                case "id" -> getProducerById(Integer.parseInt(id));
                case "username" -> getProducerByUsername(id);
                case "email" -> getProducerByEmail(id);
                default -> throw new GeneralException("Invalid user identifier.", HttpStatus.BAD_REQUEST);
            };
            boolean isSameUser = producer.getUsername().equals(securityUser.getUsername());
            if(!isSameUser)
                isAdmin = userRepository.getAdminByUsername(securityUser.getUsername()).isPresent();
            return isSameUser || isAdmin;
        } catch (GeneralException e) {
            throw new GeneralException("Producer is not in the database", HttpStatus.NOT_FOUND);
        }
    }
    public ResponseEntity<BasicMessageResponse> getAllProducers(){
        List<User> users = userRepository.getAllProducers();
        List<CinemaUserDtoG> userDtoGS =  users.stream().map(user -> {
            CinemaUserDtoG userDtoG = converter.convertToDto(user, CinemaUserDtoG.class);
            return userDtoG;
        }).toList();
        return new ResponseEntity<>(new BasicMessageResponse("Producers", userDtoGS, 200), HttpStatus.OK);
    }

    private User getProducerById(Integer id){
        return userRepository.getProducerById(id).orElseThrow(()->new GeneralException("Producer is not in the database", HttpStatus.NOT_FOUND));
    }
    private User getProducerByUsername(String username){
        return userRepository.getProducerByUsername(username).orElseThrow(()->new GeneralException("Producer is not in the database", HttpStatus.NOT_FOUND));
    }

    private User getProducerByEmail(String email){
        return userRepository.getProducerByEmail(email).orElseThrow(()->new GeneralException("Producer is not in the database", HttpStatus.NOT_FOUND));
    }
    private Double  getAverageRatingByProducerId(int producerId){
        Double rating = producerRatingRepository.getAverageRatingByProducerId(producerId);
        if (rating == null) return 0.0;
        return rating;
    }

    public ResponseEntity<BasicMessageResponse> getProducer(String producerId, String idType){
        User producer = switch (idType) {
            case "id" -> getProducerById(Integer.parseInt(producerId));
            case "username" -> getProducerByUsername(producerId);
            case "email" -> getProducerByEmail(producerId);
            default -> throw new GeneralException("Invalid user identifier.", HttpStatus.BAD_REQUEST);
        };
        try {

            double producerRating = getAverageRatingByProducerId(producer.getId());
            ProducerDto producerDto = converter.convertToEntity(producer, ProducerDto.class);
            producerDto.setRating(producerRating);

            return new ResponseEntity<>(new BasicMessageResponse("User", producerDto, 200), HttpStatus.OK);
        } catch (Exception e){
            throw new GeneralException("Could not retrieve the producer");
        }
    }


    @Transactional
    public ResponseEntity<BasicMessageResponse> deleteProducer(int id, SecurityUser securityUser){
        try {
            getProducerById(id);
            if(!validateUser(Integer.toString(id), securityUser, "id")) throw new GeneralException("User unauthorized", HttpStatus.UNAUTHORIZED);
            //delete producer's rating
            producerRatingRepository.deleteAllByProducerId(id);
            //delete producer's films
            filmService.getFilmsByProducerIdF(id).stream().forEach(film -> {
                deleteFilm(film.getId(), securityUser);
            });

            userRepository.deleteById(id);
            log.info("deleted producer.");
            return new ResponseEntity<>(new BasicMessageResponse("Message", "deleted producer", 200), HttpStatus.OK);
        }  catch (Exception e){
            log.error("could not delete producer.");
            throw new GeneralException("Could cot delete producer." + e.getMessage());
        }
    }

    public ResponseEntity<BasicMessageResponse> addFilm(FilmDto filmDto, SecurityUser securityUser) {
        try {
            User producer = getProducerById(filmDto.getProducerId());
            if(!validateUser(producer.getUsername(), securityUser, "username")) throw new GeneralException("User unauthorized", HttpStatus.UNAUTHORIZED);
            Film film = converter.convertToEntity(filmDto, Film.class);
            Producer producer1 = converter.convertToEntity(producer, Producer.class);
            film.setProducer(producer1);
            filmsValidator.validate(film);
            filmService.addFilm(film);
            log.info("added film by producer");
            return new ResponseEntity<>(new BasicMessageResponse("Message", "added film <" + film.getName() + "> by: <" + film.getProducer().getUsername() + ">", 201), HttpStatus.CREATED);
        }  catch (Exception e) {
            log.error("Could not add the film to db due to a logical error.");
            return new ResponseEntity<>(new BasicMessageResponse("Error", e.getMessage(), 400), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<BasicMessageResponse> updateFilmById(int filmId, Map<Object, Object> fields, SecurityUser securityUser){
        try{
            Film film = filmService.getFilmById(filmId);
            if(!validateUser(film.getProducer().getUsername(), securityUser, "username")) throw new GeneralException("User unauthorized", HttpStatus.UNAUTHORIZED);
            filmService.updateFilm(film, fields);
            log.info("updated film by producer");
            return new ResponseEntity<>(new BasicMessageResponse("Message", "updated film <" + film.getName() + "> by: <" + film.getProducer().getUsername() + ">", 201), HttpStatus.OK);
        }  catch (Exception e) {
            log.error("Could not update the film to db due to a logical error.");
            return new ResponseEntity<>(new BasicMessageResponse("Error", e.getMessage(), 400), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<BasicMessageResponse> deleteFilm(int filmId, SecurityUser securityUser){
        try {
            Film film = filmService.getFilmById(filmId);
            if(!validateUser(film.getProducer().getUsername(), securityUser, "username")) throw  new GeneralException("User unauthorized", HttpStatus.UNAUTHORIZED);
            //delete film ratings
            filmService.deleteFilmRatingByFilmId(filmId);
            // delete film schedules
            filmService.deleteFilmSchedulesByFilmId(filmId);
            filmService.deleteFilm(film);
            log.info("deleted film");
            return new ResponseEntity<>(new BasicMessageResponse("Message", "deleted film: " + film.getName(), 200), HttpStatus.OK);
        }  catch (Exception e){
            log.error("could not delete the film");
            throw new GeneralException("Could not delete the film");
        }
    }

    public ResponseEntity<BasicMessageResponse> getProducerAvgRating(int producerId) {
        getProducerById(producerId);
        try {
            double rating = producerRatingRepository.getAverageRatingByProducerId(producerId);
            log.info("producer rating is found.");
            return new ResponseEntity<>( new BasicMessageResponse("rating", rating+"", 200), HttpStatus.OK);
        }
        catch (Exception e){
            log.error("producer has no rating.");
            return new ResponseEntity<>( new BasicMessageResponse("rating", "0", 200), HttpStatus.OK);
        }
    }

}