package com.ps.cinema.server.controller;

import com.ps.cinema.server.BasicResponce.BasicMessageResponse;
import com.ps.cinema.server.dto.FilmDto;
import com.ps.cinema.server.model.Film;
import com.ps.cinema.server.model.FilmRating;
import com.ps.cinema.server.service.FilmService;
import com.ps.cinema.server.service.FilmServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//Any registered user can access this data.(For security films/**)
@RestController
@RequestMapping("/api/v1/film")
public class FilmController {

    @Autowired
    FilmService filmService;

    @GetMapping("")
    public ResponseEntity<BasicMessageResponse> getFilms(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false, name = "producerName") String producerName,
            @RequestParam(required = false, name = "producerId") Integer producerId
    ) {
        if (name != null) {
            return filmService.getFilmsByName(name);
        } else if (id != null) {
            return filmService.getFilmByIdR(id);
        } else if (producerName != null) {
            return filmService.getFilmsByProducerName(producerName);
        } else if (producerId != null) {
            return filmService.getFilmsByProducerId(producerId);
        } else {
            return filmService.getAllFilms();
        }
    }

    @GetMapping("/avgRate/{filmId}")
    public ResponseEntity<BasicMessageResponse> getFilmAvgRating(@PathVariable int filmId){
        return filmService.getFilmRatingByFilmId(filmId);
    }

}
