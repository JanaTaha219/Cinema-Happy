package com.ps.cinema.server.controller;

import com.ps.cinema.server.BasicResponce.BasicMessageResponse;
import com.ps.cinema.server.dto.CinemaUserDto;
import com.ps.cinema.server.dto.FilmDto;
import com.ps.cinema.server.exception.GeneralException;
import com.ps.cinema.server.model.SecurityUser;
import com.ps.cinema.server.service.CinemaAppUserService;
import com.ps.cinema.server.service.ProducerService;
import com.ps.cinema.server.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/producer")
public class ProducerController {

    @Autowired
    ProducerService producerService;

    @Autowired
    CinemaAppUserService cinemaAppUserService ;

    @Autowired
    UserUtil util;

    @PostMapping("/film")
    public ResponseEntity<BasicMessageResponse> addFilm(@RequestBody FilmDto filmDto, @AuthenticationPrincipal SecurityUser securityUser){
        return producerService.addFilm(filmDto, securityUser);
    }

    @DeleteMapping("/film/{filmId}")
    public ResponseEntity<BasicMessageResponse> deleteFilm(@PathVariable int filmId, @AuthenticationPrincipal SecurityUser securityUser){
        return producerService.deleteFilm(filmId, securityUser);
    }

    @PatchMapping("/film/{filmId}")
    public ResponseEntity<BasicMessageResponse> updateFilm(@PathVariable int filmId,  @RequestBody Map<Object, Object> fields, @AuthenticationPrincipal SecurityUser securityUser){
        return producerService.updateFilmById(filmId, fields, securityUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BasicMessageResponse> getProducer(@PathVariable int id){
        return producerService.getProducer(Integer.toString(id), "id");
    }

    @GetMapping("")
    public ResponseEntity<BasicMessageResponse> getProducer(@RequestParam(required = false) Integer id,
                                                         @RequestParam(required = false) String name,
                                                         @RequestParam(required = false) String email) {
        if (id != null) {
            return producerService.getProducer(Integer.toString(id), "id");
        } else if (name != null) {
            return producerService.getProducer(name, "name");
        } else if (email != null) {
            return producerService.getProducer(email, "email");
        } else {
            return producerService.getAllProducers();
        }
    }


    @PatchMapping("/{id}")
    public ResponseEntity<BasicMessageResponse> updateProducer(@PathVariable("id")  int id, @RequestBody Map<Object, Object> fields, @AuthenticationPrincipal SecurityUser securityUser){
        return util.updateCinemaUser(id+"", fields, "id", securityUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BasicMessageResponse> deleteProducer(@PathVariable int id, @AuthenticationPrincipal SecurityUser securityUser){
        return producerService.deleteProducer(id, securityUser);
    }

    @DeleteMapping("")
    public ResponseEntity<BasicMessageResponse> deleteProducer(@RequestParam(required = false) Integer id,
                                                             @AuthenticationPrincipal SecurityUser securityUser) {
        if (id != null) {
            return producerService.deleteProducer(id, securityUser);
        } else {
            throw new GeneralException("Invalid identifier");
        }
    }

    @GetMapping("/rate/{id}")
    public ResponseEntity<BasicMessageResponse> getProducerAvgRating(@PathVariable int id){
        return producerService.getProducerAvgRating(id);
    }
}
