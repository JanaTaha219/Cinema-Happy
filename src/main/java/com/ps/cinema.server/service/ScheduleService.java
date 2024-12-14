package com.ps.cinema.server.service;

import com.ps.cinema.server.dto.ScheduleDto;
import com.ps.cinema.server.model.Schedule;

import java.util.List;

public interface ScheduleService {
    public Schedule getScheduleById(int id);

    public List<Schedule> getAllSchedules();

    public Schedule updateSchedule(int scheduleId, ScheduleDto scheduleDto);

    public void decreaseAvailableSeats(int scheduleId, int noOfSeats);
}
