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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


@Service
@Slf4j
public class ViewerServiceImpl implements ViewerService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProducerRatingRepository producerRatingRepository;

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    FilmRatingRepository filmRatingRepository;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    UserScheduleRepository userScheduleRepository;

    @Autowired
    ObjectsValidator<FilmRating> filmRatingsValidator;

    @Autowired
    ObjectsValidator<ProducerRating> producerRatingsValidator;

    @Autowired
    EntityDtoConverter converter;

    @Autowired
    CinemaAppUserService cinemaAppUserService;

    @Autowired
    AdminService adminService;

    private boolean validateUser(String id, SecurityUser securityUser, String idType) {
        User viewer;
        boolean isAdmin=false;
        try {
            viewer = switch (idType) {
                case "id" -> getViewerById(Integer.parseInt(id));
                case "username" -> getViewerByUsername(id);
                case "email" -> getViewerByEmail(id);
                default -> throw new GeneralException("invalid user identifier.", HttpStatus.BAD_REQUEST);
            };
            boolean isSameUser = viewer.getUsername().equals(securityUser.getUsername());
            if(!isSameUser)
                isAdmin = adminService.isAdmin(securityUser.getUsername(), "username");
            return (isAdmin||isSameUser);
        } catch (GeneralException e) {
            return false;
        }
    }


    private User getViewerById(int id) {
        return userRepository.getViewerById(id).orElseThrow(()-> new GeneralException("viewer is not in the database", HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<BasicMessageResponse> getViewerByIdR(int id){
        CinemaUserDtoG cinemaUserDtoG = converter.convertToDto(getViewerById(id), CinemaUserDtoG.class);
        return new ResponseEntity<>
                (new BasicMessageResponse("User", cinemaUserDtoG, 200), HttpStatus.OK);
    }

    public ResponseEntity<BasicMessageResponse> getViewerByNameR(String username){
        CinemaUserDtoG cinemaUserDtoG = converter.convertToDto(getViewerByUsername(username), CinemaUserDtoG.class);
        return new ResponseEntity<>
                (new BasicMessageResponse("User", cinemaUserDtoG, 200), HttpStatus.OK);
    }

    public ResponseEntity<BasicMessageResponse> getViewerByEmailR(String email){
        CinemaUserDtoG cinemaUserDtoG = converter.convertToDto(getViewerByEmail(email), CinemaUserDtoG.class);
        return new ResponseEntity<>
                (new BasicMessageResponse("User", cinemaUserDtoG, 200), HttpStatus.OK);
    }

    public ResponseEntity<BasicMessageResponse> getAllViewers(){
        List<User> users = userRepository.getAllViewers();
        List<CinemaUserDtoG> userDtoGS =  users.stream().map(user -> {
            CinemaUserDtoG userDtoG = converter.convertToDto(user, CinemaUserDtoG.class);
            return userDtoG;
        }).toList();
        return new ResponseEntity<>(new BasicMessageResponse("Users", userDtoGS, 200), HttpStatus.OK);
    }



    private User getViewerByEmail(String email){
        return userRepository.getViewerByEmail(email).orElseThrow(()-> new GeneralException("viewer is not in the database", HttpStatus.NOT_FOUND));
    }

    private User getViewerByUsername(String username){
        return userRepository.getViewerByUsername(username).orElseThrow(()-> new GeneralException("viewer is not in the database", HttpStatus.NOT_FOUND));
    }

    private User getProducerById(int id) {
        return userRepository.getProducerById(id).orElseThrow(()->new GeneralException("producer is not in the database", HttpStatus.NOT_FOUND));
    }

    private Film getFilmById(int id) {
        return filmRepository.findById(id).orElseThrow(()-> new GeneralException("film is not in the database", HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<BasicMessageResponse> rateProducer(ProducerRatingDto producerRatingDto, SecurityUser securityUser) {
        try {
            if(!validateUser(Integer.toString(producerRatingDto.getViewerId()), securityUser, "id")) new GeneralException("User unauthorized", HttpStatus.UNAUTHORIZED);
            User viewer = getViewerById(producerRatingDto.getViewerId());
            User producer = getProducerById(producerRatingDto.getProducerId());
            Optional<ProducerRating> rating= producerRatingRepository.findByProducerIdAndViewerId(producerRatingDto.getProducerId(), producerRatingDto.getViewerId());
            if(rating.isPresent()) throw new GeneralException("User can have one rate only per producer.", HttpStatus.BAD_REQUEST);
            ProducerRating producerRating = new ProducerRating(converter.convertToEntity(viewer, Viewer.class), converter.convertToEntity(producer, Producer.class), producerRatingDto.getRating());
            producerRatingsValidator.validate(producerRating);
            producerRatingRepository.save(producerRating);
            log.info("producer " + producer.getUsername() + " is rated by " + viewer.getUsername() + " : " + producerRatingDto.getRating() + " out of 5");
            return new ResponseEntity<>(new BasicMessageResponse("message", "rated producer " + producer.getUsername() + " " + producerRatingDto.getRating() + " out of 5 by " + viewer.getUsername(), 201), HttpStatus.CREATED);
        }
        catch (Exception e){
            log.error("could not rate the producer due to a logical error");
            throw new GeneralException("Could not rate the producer. "+e.getMessage());
        }
    }

    public ResponseEntity<BasicMessageResponse> rateFilm(FilmRatingDto filmRatingDto, SecurityUser securityUser) {
        try {
            if(!validateUser(Integer.toString(filmRatingDto.getViewerId()), securityUser, "id")) throw new GeneralException("User unauthorized", HttpStatus.UNAUTHORIZED);
            User viewer = getViewerById(filmRatingDto.getViewerId());
            Film film = getFilmById(filmRatingDto.getFilmId());
            Optional<FilmRating> rating= filmRatingRepository.findByFilmIdAndViewerId(filmRatingDto.getFilmId(), filmRatingDto.getViewerId());
            if(rating.isPresent()) throw new GeneralException("User can have one rate only per film", HttpStatus.BAD_REQUEST);
            FilmRating filmRating = new FilmRating(converter.convertToEntity(viewer, Viewer.class), film, filmRatingDto.getRating());
            filmRatingsValidator.validate(filmRating);
            filmRatingRepository.save(filmRating);
            log.info("rated film " + film.getName() + " by " + viewer.getUsername() + " : " + filmRatingDto.getRating() + " out of 5");
            return new ResponseEntity<>(new BasicMessageResponse("message", "rated film " + film.getName() + " " + filmRatingDto.getRating() + " out of 5 by " + viewer.getUsername(), 201), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("could not rate the film due to a logical error");
            throw new GeneralException("Could not rate the film");
        }
    }

    public ResponseEntity<BasicMessageResponse> scheduleFilm(UserScheduleDto userScheduleDto, SecurityUser securityUser){
        try {
            if(!validateUser(Integer.toString(userScheduleDto.getViewer_id()), securityUser, "id")) throw new GeneralException("User unauthorized", HttpStatus.UNAUTHORIZED);
            Schedule schedule = scheduleService.getScheduleById(userScheduleDto.getSchdule_id());
            User viewer = getViewerById(userScheduleDto.getViewer_id());
            UserSchedule userSchedule = new UserSchedule(converter.convertToEntity(viewer, Viewer.class), schedule);
            scheduleService.decreaseAvailableSeats(userScheduleDto.getSchdule_id(), 1);
            userScheduleRepository.save(userSchedule);
            log.info("Scheduled for viewer");
            return new ResponseEntity<>(new BasicMessageResponse("Message", "Scheduled for viewer", 201), HttpStatus.CREATED);
        } catch (Exception e){
            log.error("Could not Schedule for viewer");
            throw new GeneralException("Could not Schedule for viewer", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<BasicMessageResponse> updateFilmRating(Map<Object, Object> fields, SecurityUser securityUser) {
        try {

            User v = getViewerById((Integer) fields.get("viewer"));
            if(!validateUser(Integer.toString(v.getId()), securityUser, "id"))
                throw new GeneralException("User unauthorized", HttpStatus.UNAUTHORIZED);
            getFilmById((Integer) fields.get("film"));
            FilmRating filmRating = filmRatingRepository.findByFilmIdAndViewerId((Integer) fields.get("film"), (Integer) fields.get("viewer")).orElseThrow(() -> new GeneralException("viewer did not rate this film.", HttpStatus.BAD_REQUEST));
            filmRating.setRating((Integer) fields.get("rating"));
            filmRatingsValidator.validate(filmRating);
            filmRatingRepository.save(filmRating);
            log.info("updated film rating");
            return new ResponseEntity<>(new BasicMessageResponse("Message", "Updated rating", 200), HttpStatus.OK);
        }  catch (Exception e){
            log.error("could not update film rating");
            throw new GeneralException("Could not update film rating.", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<BasicMessageResponse> updateProducerRating(Map<Object, Object> fields, SecurityUser securityUser) {
        User v = getViewerById((Integer) fields.get("viewer"));
        if(!validateUser(Integer.toString(v.getId()), securityUser, "id"))
            throw new GeneralException("User unauthorized", HttpStatus.UNAUTHORIZED);
        getProducerById((Integer) fields.get("producer"));
        try {
            ProducerRating producerRating = producerRatingRepository.findByProducerIdAndViewerId((Integer) fields.get("producer"), (Integer) fields.get("viewer")).orElseThrow(() -> new GeneralException("viewer did not rate this producer.", HttpStatus.BAD_REQUEST));
            producerRating.setRating((Integer) fields.get("rating"));
            producerRatingsValidator.validate(producerRating);
            producerRatingRepository.save(producerRating);
            log.info("updated producer rating");
            return new ResponseEntity<>(new BasicMessageResponse("Message", "Updated rating", 200), HttpStatus.OK);
        }  catch (Exception e){
            log.error("could not update producer rating");
            throw new GeneralException("Could not update producer rating.", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<BasicMessageResponse> getFilmRating(int viewerId, int filmId){
        getViewerById(viewerId);
        getFilmById(filmId);
        FilmRating filmRating = filmRatingRepository.findByFilmIdAndViewerId(filmId, viewerId).orElseThrow(() ->  new GeneralException("viewer did not rate this film", HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(new BasicMessageResponse("rating", filmRating.getRating(), 200), HttpStatus.OK);
    }

    public ResponseEntity<BasicMessageResponse> getProducerRating(int viewerId, int producerId){
        getProducerById(producerId);
        getViewerById(viewerId);
        ProducerRating producerRating = producerRatingRepository.findByProducerIdAndViewerId(producerId, viewerId).orElseThrow(() -> new GeneralException("viewer did not rate this producer", HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(new BasicMessageResponse("rating",  producerRating.getRating(), 200), HttpStatus.OK);
    }


    public ResponseEntity<BasicMessageResponse> deleteViewer(String idType, String id, SecurityUser securityUser){
        User viewer = switch (idType) {
            case ("id") -> getViewerById(Integer.parseInt(id));
            case ("username") -> getViewerByUsername(id);
            case ("email") -> getViewerByEmail(id);
            default -> throw new GeneralException("invalid user identifier.", HttpStatus.BAD_REQUEST);
        };
        int viewerId = viewer.getId();
        try{
            if(!validateUser(Integer.toString(viewerId), securityUser, "id"))
                throw new GeneralException("User unauthorized", HttpStatus.UNAUTHORIZED);
            //delete viewer's ratings(producer & film)
            producerRatingRepository.deleteAllByViewerId(viewerId);
            filmRatingRepository.deleteAllByViewerId(viewerId);
            //delete viewer reservations
            userScheduleRepository.deleteAllByViewerId(viewerId);
            //delete viewer
            userRepository.deleteById(viewerId);
            log.info("deleted viewer.");
            return new ResponseEntity<>(new BasicMessageResponse("Message", "deleted viewer with id" + viewerId, 200), HttpStatus.OK);
        }  catch (Exception e){
            log.error("Could not delete the viewer");
            throw new GeneralException("Could not delete the viewer.");
        }
    }
}