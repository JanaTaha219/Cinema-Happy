package com.ps.cinema.server.service;

import com.ps.cinema.server.BasicResponce.BasicMessageResponse;
import com.ps.cinema.server.dto.FilmRatingDto;
import com.ps.cinema.server.dto.ProducerRatingDto;
import com.ps.cinema.server.dto.UserScheduleDto;
import com.ps.cinema.server.model.SecurityUser;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ViewerService {
    public ResponseEntity<BasicMessageResponse> rateProducer(ProducerRatingDto producerRatingDto, SecurityUser securityUser);
    public ResponseEntity<BasicMessageResponse> rateFilm(FilmRatingDto filmRatingDto, SecurityUser securityUser);
    public ResponseEntity<BasicMessageResponse> scheduleFilm(UserScheduleDto userScheduleDto, SecurityUser securityUser);
    public ResponseEntity<BasicMessageResponse> updateFilmRating(Map<Object, Object> fields, SecurityUser securityUser);
    public ResponseEntity<BasicMessageResponse> updateProducerRating(Map<Object, Object> fields, SecurityUser securityUser);
    public ResponseEntity<BasicMessageResponse> getFilmRating(int viewerId, int filmId);
    public ResponseEntity<BasicMessageResponse> getProducerRating(int viewerId, int producerId);
    public ResponseEntity<BasicMessageResponse> deleteViewer(String idType, String id, SecurityUser securityUser);
    public ResponseEntity<BasicMessageResponse> getViewerByIdR(int id);
    public ResponseEntity<BasicMessageResponse> getViewerByNameR(String username);
    public ResponseEntity<BasicMessageResponse> getViewerByEmailR(String email);
    public ResponseEntity<BasicMessageResponse> getAllViewers();
    }
