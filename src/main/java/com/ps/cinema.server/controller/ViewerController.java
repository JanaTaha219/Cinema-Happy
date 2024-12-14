package com.ps.cinema.server.controller;

//sho dayel?
/*
1- Admin adds a schedule
2- get a film rating
3- get a producer rating
4- delete a user
5- Add a user :'(
6- Security
* */
import com.ps.cinema.server.BasicResponce.BasicMessageResponse;
import com.ps.cinema.server.dto.FilmRatingDto;
import com.ps.cinema.server.dto.ProducerRatingDto;
import com.ps.cinema.server.dto.UserScheduleDto;
import com.ps.cinema.server.exception.GeneralException;
import com.ps.cinema.server.model.SecurityUser;
import com.ps.cinema.server.service.CinemaAppUserServiceImpl;
import com.ps.cinema.server.service.ViewerService;

import com.ps.cinema.server.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/viewer")
public class ViewerController {

    @Autowired
    CinemaAppUserServiceImpl cinemaAppUserService;

    @Autowired
    ViewerService viewerService;

    @Autowired
    UserUtil util;

    //Anyone can access this end point(even if they're not registered)
//    @PostMapping("")
//    public ResponseEntity<BasicMessageResponse> addViewer(@RequestBody CinemaUserDto cinemaUserDto){
//        return cinemaAppUserService.addCinemaAppUser(cinemaUserDto, 2);
//    }

    @GetMapping("")
    public ResponseEntity<BasicMessageResponse> getViewer(@RequestParam(required = false) Integer id,
                                                          @RequestParam(required = false) String name,
                                                          @RequestParam(required = false) String email) {
        if (id != null) {
            return viewerService.getViewerByIdR(id);
        } else if (name != null) {
            return viewerService.getViewerByNameR(name);
        } else if (email != null) {
            return viewerService.getViewerByEmailR(email);
        } else {
            return viewerService.getAllViewers();
        }
    }


    @DeleteMapping("")
    public ResponseEntity<BasicMessageResponse> deleteViewer(@RequestParam(required = false) Integer id,
                                                          @RequestParam(required = false) String name,
                                                          @RequestParam(required = false) String email,
                                                          @AuthenticationPrincipal SecurityUser securityUser) {
        if (id != null) {
            return viewerService.deleteViewer("id", Integer.toString(id), securityUser);
        } else if (name != null) {
            return viewerService.deleteViewer("username", name, securityUser);
        } else if (email != null) {
            return viewerService.deleteViewer("email", email, securityUser);
        } else {
            throw new GeneralException("Invalid identifier");
        }
    }

    @PatchMapping("")
    public ResponseEntity<BasicMessageResponse> updateViewer(@RequestParam(required = false) Integer id,
                                                             @RequestParam(required = false) String name,
                                                             @RequestParam(required = false) String email,
                                                             @RequestBody Map<Object, Object> fields,
                                                             @AuthenticationPrincipal SecurityUser securityUser) {
        if (id != null) {
            return util.updateCinemaUser(id+"", fields, "id", securityUser);
        } else if (name != null) {
            return util.updateCinemaUser(name, fields, "username", securityUser);
        } else if (email != null) {
            return util.updateCinemaUser(email, fields, "email", securityUser);
        } else {
            throw new GeneralException("Invalid identifier");
        }
    }

    @PostMapping("/rateProducer")
    public ResponseEntity<BasicMessageResponse> rateProducer(@RequestBody ProducerRatingDto producerRatingDto, @AuthenticationPrincipal SecurityUser securityUser) {
        return viewerService.rateProducer(producerRatingDto, securityUser);
    }

    @PostMapping("/rateFilm")
    public ResponseEntity<BasicMessageResponse> rateFilm(@RequestBody FilmRatingDto filmRatingDto, @AuthenticationPrincipal SecurityUser securityUser) {
        return viewerService.rateFilm(filmRatingDto, securityUser);
    }

    @PostMapping("/schedule")
    public ResponseEntity<BasicMessageResponse> scheduleFilm(@RequestBody UserScheduleDto userScheduleDto, @AuthenticationPrincipal SecurityUser securityUser){
        return viewerService.scheduleFilm(userScheduleDto, securityUser);
    }

    @PatchMapping("/rateFilm")
    public ResponseEntity<BasicMessageResponse> updateFilmRate(@RequestBody Map<Object, Object> fields, @AuthenticationPrincipal SecurityUser securityUser){
        return viewerService.updateFilmRating(fields, securityUser);
    }

    @PatchMapping("/rateProducer")
    public ResponseEntity<BasicMessageResponse> updateProducerRate(@RequestBody Map<Object, Object> fields, @AuthenticationPrincipal SecurityUser securityUser){
        return viewerService.updateProducerRating(fields, securityUser);
    }

    @GetMapping("/{id}/producer/{producerId}")
    public ResponseEntity<BasicMessageResponse> getProducerRating(@PathVariable int id, @PathVariable int producerId){
        return viewerService.getProducerRating(id, producerId);
    }

    @GetMapping("/{id}/film/{filmId}")
    public ResponseEntity<BasicMessageResponse> getFilmRating(@PathVariable int id, @PathVariable int filmId){
        return viewerService.getFilmRating(id, filmId);
    }
}