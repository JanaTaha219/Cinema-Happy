package com.ps.cinema.server.controller;

import com.ps.cinema.server.BasicResponce.BasicMessageResponse;
import com.ps.cinema.server.model.Schedule;
import com.ps.cinema.server.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @GetMapping("")
    public List<Schedule> getAllSchedules(){
        return scheduleService.getAllSchedules();
    }

    @GetMapping("/{scheduleId}")
    public Schedule getScheduleById(@PathVariable int scheduleId){
        return scheduleService.getScheduleById(scheduleId);
    }

}
