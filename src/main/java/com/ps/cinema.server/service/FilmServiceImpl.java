package com.ps.cinema.server.service;

import com.ps.cinema.server.BasicResponce.BasicMessageResponse;
import com.ps.cinema.server.dto.FilmDto;
import com.ps.cinema.server.exception.GeneralException;

import com.ps.cinema.server.mapper.EntityDtoConverter;
import com.ps.cinema.server.model.Film;
import com.ps.cinema.server.model.FilmRating;
import com.ps.cinema.server.model.User;
import com.ps.cinema.server.repository.*;
import com.ps.cinema.server.validator.ObjectsValidator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    ObjectsValidator<Film> filmsValidator;

    @Autowired
    FilmRatingRepository filmRatingRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    EntityDtoConverter converter;

    @Autowired
    UserRepository userRepository;

    private User getProducerById(int producerId){
        return userRepository.getProducerById(producerId).orElseThrow(() -> new GeneralException("Producer is not in the database.", HttpStatus.NOT_FOUND));
    }

    private User getProducerByName(String name){
        return userRepository.getProducerByUsername(name).orElseThrow(() -> new GeneralException("Producer is not in the database.", HttpStatus.NOT_FOUND));
    }


    public ResponseEntity<BasicMessageResponse> getAllFilms() {
        List<Film> films = filmRepository.findAll();
        List<FilmDto> filmDtos =  films.stream().map(film -> {
            FilmDto filmDto = converter.convertToDto(film, FilmDto.class);
            filmDto.setTotalRating(getFilmAvgRatingByFilmId(film.getId()));
            return filmDto;
        }).toList();
        return new ResponseEntity<>(new BasicMessageResponse("Films", filmDtos, 200), HttpStatus.OK);
    }

    public ResponseEntity<BasicMessageResponse> getFilmByIdR(int id){
        Film film = getFilmById(id);
        film.setTotalRating(getFilmAvgRatingByFilmId(id));
        FilmDto filmDto = converter.convertToDto(film, FilmDto.class);
        return new ResponseEntity<>(new BasicMessageResponse("Film", filmDto, 200), HttpStatus.OK);
    }
    public Film getFilmById(int id){
        return filmRepository.findById(id).orElseThrow(() -> new GeneralException("film is not in the database.", HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<BasicMessageResponse> getFilmsByName(String name){
        List<Film> films = filmRepository.findByNameContaining(name);
        List<FilmDto> filmDtos =  films.stream().map(film -> {
            FilmDto filmDto = converter.convertToDto(film, FilmDto.class);
            filmDto.setTotalRating(getFilmAvgRatingByFilmId(film.getId()));
            return filmDto;
        }).toList();
        return new ResponseEntity<>(new BasicMessageResponse("Films", filmDtos, 200), HttpStatus.OK);
    }

    public ResponseEntity<BasicMessageResponse> getFilmsByProducerId(int producerId){
        getProducerById(producerId);
        List<Film> films = filmRepository.findByProducerId(producerId);
        List<FilmDto> filmDtos =  films.stream().map(film -> {
            FilmDto filmDto = converter.convertToDto(film, FilmDto.class);
            filmDto.setTotalRating(getFilmAvgRatingByFilmId(film.getId()));
            return filmDto;
        }).toList();
        return new ResponseEntity<>(new BasicMessageResponse("Films", filmDtos, 200), HttpStatus.OK);
    }

    public List<Film> getFilmsByProducerIdF(int producerId){
        getProducerById(producerId);
        return filmRepository.findByProducerId(producerId);
    }

    public ResponseEntity<BasicMessageResponse> getFilmsByProducerName(String producerName){
        getProducerByName(producerName);
        List<Film> films = filmRepository.findByProducerUsername(producerName);
        List<FilmDto> filmDtos =  films.stream().map(film -> {
            FilmDto filmDto = converter.convertToDto(film, FilmDto.class);
            filmDto.setTotalRating(getFilmAvgRatingByFilmId(film.getId()));
            return filmDto;
        }).toList();
        return new ResponseEntity<>(new BasicMessageResponse("Films", filmDtos, 200), HttpStatus.OK);

    }

    public Film getFilmByName(String name){
        return filmRepository.findByName(name).orElseThrow(() -> new GeneralException("Film is not in the database.", HttpStatus.NOT_FOUND));
    }

    public Film addFilm(Film film){
        return filmRepository.save(film);
    }

    public Film updateFilm(Film film, Map<Object, Object> fields){
        try{
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findRequiredField(Film.class, (String) key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, film, value);
            });
            filmsValidator.validate(film);
            filmRepository.save(film);
            log.info("Hello from film service.");
            return film;
        } catch (Exception e){
            log.error("Film is not updated due to a logical error(from service)");
            throw new GeneralException("Film is not updated due to an error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //delete all user's rating by user id
    //delete all user's rating by user name

    public List<FilmRating> getFilmRatingsByFilmId(int filmID){
        return filmRatingRepository.findByFilmId(filmID);
    }

    @Transactional
    public void deleteFilmRatingByFilmId(int filmId){
        filmRatingRepository.deleteAllByFilmId(filmId);
    }

    @Transactional
    public void deleteViewerRatingByViewerId(int viewerID){
        filmRatingRepository.deleteAllByViewerId(viewerID);
    }

    @Transactional
    public void deleteViewerRatingByViewerName(String viewerName){
        filmRatingRepository.deleteAllByViewerUsername(viewerName);
    }

    @Transactional
    public void deleteFilmsByProducerId(int producerId){
        filmRepository.deleteAllByProducerId(producerId);
    }

    @Transactional
    public void deleteFilmSchedulesByFilmId(int filmId){scheduleRepository.deleteAllByFilmId(filmId);}

    private double getFilmAvgRatingByFilmId(int filmId){
        Double rating = filmRatingRepository.getAverageRatingByFilmId(filmId);
        if(rating == null) return 0;
        return filmRatingRepository.getAverageRatingByFilmId(filmId);
    }

    public ResponseEntity<BasicMessageResponse> getFilmRatingByFilmId(int filmId){
        getFilmById(filmId);
        try {
            double rating = filmRatingRepository.getAverageRatingByFilmId(filmId);
            log.info("Film rating is found.");
            return new ResponseEntity<>( new BasicMessageResponse("rating", rating+"", 200), HttpStatus.OK);
        }
        catch (Exception e){
            log.error("Film has no rating.");
            return new ResponseEntity<>( new BasicMessageResponse("rating", "0", 200), HttpStatus.OK);
        }
    }

    public void deleteFilm(Film film){
        filmRepository.delete(film);
    }
}
