package com.ps.cinema.server.service;

import com.ps.cinema.server.dto.ScheduleDto;
import com.ps.cinema.server.exception.GeneralException;
import com.ps.cinema.server.model.Film;
import com.ps.cinema.server.model.Schedule;
import com.ps.cinema.server.repository.FilmRepository;
import com.ps.cinema.server.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService{
    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    FilmRepository filmRepository;

    public Schedule getScheduleById(int id){
        return scheduleRepository.findById(id).orElseThrow(() -> new GeneralException("schedule is not in the database.", HttpStatus.NOT_FOUND));
    }
    public List<Schedule> getAllSchedules(){
        return scheduleRepository.findAll();
    }

    public void deleteSchedule(int scheduleId){
        scheduleRepository.deleteById(scheduleId);
    }

    public void decreaseAvailableSeats(int scheduleId, int noOfSeats) {
        Schedule schedule = getScheduleById(scheduleId);
        try {
            if ((schedule.getAvailableSeats() + noOfSeats) > 0) {
                int currentSeats = schedule.getAvailableSeats();
                schedule.setAvailableSeats(currentSeats - noOfSeats);
                scheduleRepository.save(schedule);
            } else {
                throw new GeneralException("no available seats left.");
            }
        } catch (Exception e){
            throw new GeneralException("Could not reserve a seat.");
        }
    }

    public Schedule updateSchedule(int scheduleId, ScheduleDto scheduleDto){
        Schedule schedule = getScheduleById(scheduleId);
        if(scheduleDto.getShowDateTime() != null)
            schedule.setShowDateTime(scheduleDto.getShowDateTime());
        if (scheduleDto.getPrice() != 0)
            schedule.setPrice(scheduleDto.getPrice());
        if(scheduleDto.getFilmId() != 0){
            Film film = filmRepository.findById(scheduleDto.getFilmId()).orElseThrow(() -> new GeneralException("Film is not in the database.", HttpStatus.NOT_FOUND));
            schedule.setFilm(film);
        }
        return schedule;
    }
}