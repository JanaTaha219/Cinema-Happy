package com.ps.cinema.server.service;

import com.ps.cinema.server.BasicResponce.BasicMessageResponse;
import com.ps.cinema.server.dto.FilmDto;
import com.ps.cinema.server.model.Film;
import com.ps.cinema.server.model.FilmRating;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface FilmService {
    public ResponseEntity<BasicMessageResponse> getAllFilms();

    public ResponseEntity<BasicMessageResponse> getFilmByIdR(int id);

    public Film getFilmById(int id);

    public ResponseEntity<BasicMessageResponse> getFilmsByName(String name);

    public ResponseEntity<BasicMessageResponse> getFilmsByProducerId(int p);

    public ResponseEntity<BasicMessageResponse> getFilmsByProducerName(String p);

    public Film addFilm(Film film);

    public Film updateFilm(Film film, Map<Object, Object> fields);

    public Film getFilmByName(String name);

    public void deleteViewerRatingByViewerId(int viewerID);

    public void deleteViewerRatingByViewerName(String viewerName);

    List<FilmRating> getFilmRatingsByFilmId(int filmID);

    public void deleteFilmRatingByFilmId(int filmId);

    public ResponseEntity<BasicMessageResponse> getFilmRatingByFilmId(int filmId);

    public void deleteFilm(Film film);
    public void deleteFilmSchedulesByFilmId(int filmId);

    public void deleteFilmsByProducerId(int producerId);
    public List<Film> getFilmsByProducerIdF(int producerId);

}