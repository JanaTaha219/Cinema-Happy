package com.ps.cinema.server.service;

import com.ps.cinema.server.BasicResponce.BasicMessageResponse;
import com.ps.cinema.server.dto.CinemaUserDtoG;
import com.ps.cinema.server.dto.ScheduleDto;
import com.ps.cinema.server.exception.GeneralException;
import com.ps.cinema.server.mapper.EntityDtoConverter;
import com.ps.cinema.server.model.*;
import com.ps.cinema.server.repository.ScheduleRepository;
import com.ps.cinema.server.repository.UserRepository;
import com.ps.cinema.server.repository.UserScheduleRepository;
import com.ps.cinema.server.validator.ObjectsValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
@Slf4j
public class AdminServiceImpl implements AdminService{

    @Autowired
    EntityDtoConverter converter;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    ObjectsValidator<Schedule> schedulesValidator;
    @Autowired
    FilmService filmService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    CinemaAppUserService cinemaAppUserService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserScheduleRepository userScheduleRepository;

    private Schedule getSchedule(int scheduleId){ return scheduleRepository.findById(scheduleId).orElseThrow(()->new GeneralException("schedule is not found in the database", HttpStatus.NOT_FOUND));}
    private User getAdminById(int id) {
        return userRepository.getAdminById(id).orElseThrow(()-> new GeneralException("admin is not found in the database", HttpStatus.NOT_FOUND));
    }
    private User getAdminByUsername(String name) {
        return userRepository.getAdminByUsername(name).orElseThrow(()-> new GeneralException("admin is not found in the database", HttpStatus.NOT_FOUND));
    }
    private User getAdminByEmail(String email) {
        return userRepository.getAdminByEmail(email).orElseThrow(()-> new GeneralException("admin is not found in the database", HttpStatus.NOT_FOUND));
    }

    public boolean isAdmin(String id, String idType){
        try {
            return switch (idType) {
                case ("id") -> {
                    getAdminById(Integer.parseInt(id));
                    yield true;
                }
                case ("username") -> {
                    getAdminByUsername(id);
                    yield true;
                }
                case ("email") -> {
                    getAdminByEmail(id);
                    yield true;
                }
                default -> throw new GeneralException("invalid user identifier.", HttpStatus.BAD_REQUEST);
            };
        } catch (GeneralException e) {
            return false;
        }
    }

    public ResponseEntity<BasicMessageResponse> getAdminByIdR(int id){
        CinemaUserDtoG cinemaUserDtoG = converter.convertToDto(getAdminById(id), CinemaUserDtoG.class);
        return new ResponseEntity<>
                (new BasicMessageResponse("User", cinemaUserDtoG, 200), HttpStatus.OK);

    }

    public ResponseEntity<BasicMessageResponse> getAllAdmins(){
        List<User> users = userRepository.getAllAdmins();
        List<CinemaUserDtoG> userDtoGS =  users.stream().map(user -> {
            CinemaUserDtoG userDtoG = converter.convertToDto(user, CinemaUserDtoG.class);
            return userDtoG;
        }).toList();
        return new ResponseEntity<>(new BasicMessageResponse("Admins", userDtoGS, 200), HttpStatus.OK);
    }

    public ResponseEntity<BasicMessageResponse> getAdminByUsernameR(String username){
        CinemaUserDtoG cinemaUserDtoG = converter.convertToDto(getAdminByUsername(username), CinemaUserDtoG.class);
        return new ResponseEntity<>
                (new BasicMessageResponse("Admin", cinemaUserDtoG, 200), HttpStatus.OK);

    }

    public ResponseEntity<BasicMessageResponse> getAdminByEmailR(String email){
        CinemaUserDtoG cinemaUserDtoG = converter.convertToDto(getAdminByEmail(email), CinemaUserDtoG.class);
        return new ResponseEntity<>
                (new BasicMessageResponse("Admin", cinemaUserDtoG, 200), HttpStatus.OK);

    }

    public ResponseEntity<BasicMessageResponse> addSchedule(ScheduleDto scheduleDto) {
        try {
            Film film = filmService.getFilmById(scheduleDto.getFilmId());
            Schedule schedule = new Schedule(scheduleDto.getPrice(), film, scheduleDto.getAvailableSeats(), scheduleDto.getShowDateTime());
            schedulesValidator.validate(schedule);
            scheduleRepository.save(schedule);
            log.info("added schedule");
            return new ResponseEntity<>(new BasicMessageResponse("message", "added schedule for film:" + scheduleDto.getFilmId() + " on " + scheduleDto.getShowDateTime() + " with price: " + scheduleDto.getPrice() + " and available seats " + scheduleDto.getAvailableSeats(), 201), HttpStatus.CREATED);
        } catch (Exception e){
            log.error("could not add schedule");
            throw new GeneralException("could not add schedule." + (e.getMessage() != null ?" because of:" + e.getMessage():""));
        }
    }
    public ResponseEntity<BasicMessageResponse> deleteAdmin(String idType, String id){
        User admin = switch (idType) {
            case ("id") -> getAdminById(Integer.parseInt(id));
            case ("username") -> getAdminByUsername(id);
            case ("email") -> getAdminByEmail(id);
            default -> throw new GeneralException("invalid id type", HttpStatus.BAD_REQUEST);
        };
        try{
            userRepository.delete(admin);
            log.info("deleted admin.");
            return new ResponseEntity<>(new BasicMessageResponse("Message", "deleted admin with id" + admin.getId(), 200), HttpStatus.OK);
        } catch (Exception e){
            log.error("Could not delete the admin");
            throw new GeneralException("Could not delete the admin.");
        }
    }

    public ResponseEntity<BasicMessageResponse> deleteSchedule(int scheduleId){
        Schedule schedule = getSchedule(scheduleId);
        try {
            userScheduleRepository.deleteAllByScheduleId(scheduleId);
            scheduleRepository.delete(schedule);
            log.info("schedule is deleted");
            return new ResponseEntity<>(new BasicMessageResponse("Message", "deleted schedule with id"+scheduleId, 200), HttpStatus.OK);
        } catch (Exception e){
            log.error("Could not delete the schedule.");
            throw new GeneralException("Could not delete the schedule.");
        }
    }

    public ResponseEntity<BasicMessageResponse> updateSchedule(int scheduleId, ScheduleDto scheduleDto){
        getSchedule(scheduleId);
        try{
            Schedule schedule = scheduleService.updateSchedule(scheduleId, scheduleDto);
            scheduleRepository.save(schedule);
            log.info("update schedule.");
            return new ResponseEntity<>(new BasicMessageResponse("Message", "update schedule", 200), HttpStatus.OK);
        } catch (Exception e){
            log.error("Could not update schedule");
            throw new GeneralException("Could not update schedule"  + (e.getMessage() != null ?" because of:" + e.getMessage():"") );
        }
    }

}
