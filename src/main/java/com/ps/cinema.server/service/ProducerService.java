package com.ps.cinema.server.service;

import com.ps.cinema.server.BasicResponce.BasicMessageResponse;
import com.ps.cinema.server.dto.FilmDto;
import com.ps.cinema.server.model.SecurityUser;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ProducerService {
    public ResponseEntity<BasicMessageResponse> addFilm(FilmDto filmDto, SecurityUser securityUser);

    public ResponseEntity<BasicMessageResponse> updateFilmById(int filmId, Map<Object, Object> fields, SecurityUser securityUser);

    public ResponseEntity<BasicMessageResponse> deleteFilm(int filmId, SecurityUser securityUser);

    public ResponseEntity<BasicMessageResponse> getProducer(String producerId, String idType);

    public ResponseEntity<BasicMessageResponse> deleteProducer(int id, SecurityUser securityUser);

    public ResponseEntity<BasicMessageResponse> getProducerAvgRating(int producerId);
    public ResponseEntity<BasicMessageResponse> getAllProducers();
}
