package com.ps.cinema.server.controller;

import com.ps.cinema.server.BasicResponce.BasicMessageResponse;
import com.ps.cinema.server.dto.CinemaUserDto;
import com.ps.cinema.server.dto.ScheduleDto;
import com.ps.cinema.server.exception.GeneralException;
import com.ps.cinema.server.model.SecurityUser;
import com.ps.cinema.server.service.AdminService;
import com.ps.cinema.server.service.CinemaAppUserService;
import com.ps.cinema.server.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    CinemaAppUserService cinemaAppUserService;

    @Autowired
    UserUtil util;


    @GetMapping("")
    public ResponseEntity<BasicMessageResponse> getAdmin(@RequestParam(required = false) Integer id,
                                                          @RequestParam(required = false) String name,
                                                          @RequestParam(required = false) String email) {
        if (id != null) {
            return adminService.getAdminByIdR(id);
           // return new ResponseEntity<>(new BasicMessageResponse("hi","hi",200), HttpStatus.OK);
        } else if (name != null) {
            return adminService.getAdminByUsernameR(name);
        } else if (email != null) {
            return adminService.getAdminByEmailR(email);
        } else {
            return adminService.getAllAdmins();
        }
    }

    @PatchMapping("")
    public ResponseEntity<BasicMessageResponse> updateAdmin(@RequestParam(required = false) Integer id,
                                                             @RequestParam(required = false) String name,
                                                             @RequestParam(required = false) String email,
                                                             @RequestBody Map<Object, Object> fields,
                                                             @AuthenticationPrincipal SecurityUser securityUser) {
        if (id != null) {
            return util.updateCinemaUser(id+"", fields, "id", securityUser);
        } else if (name != null) {
            return util.updateCinemaUser(name, fields, "username", securityUser);
        } else if (email != null) {
            return util.updateCinemaUser(email, fields, "email", securityUser);
        } else {
            throw new GeneralException("Invalid identifier");
        }
    }

    @DeleteMapping("")
    public ResponseEntity<BasicMessageResponse> deleteAdmin(@RequestParam(required = false) Integer id,
                                                             @RequestParam(required = false) String name,
                                                             @RequestParam(required = false) String email,
                                                             @AuthenticationPrincipal SecurityUser securityUser) {
        if (id != null) {
            return  adminService.deleteAdmin("id", Integer.toString(id));
        } else if (name != null) {
            return adminService.deleteAdmin("username", name);
        } else if (email != null) {
            return adminService.deleteAdmin("email", email);
        } else {
            throw new GeneralException("Invalid identifier");
        }
    }

    @PostMapping("/schedule")
    public ResponseEntity<BasicMessageResponse> addSchedule(@RequestBody ScheduleDto scheduleDto){
        return adminService.addSchedule(scheduleDto);
    }

    @DeleteMapping("/schedule/{scheduleId}")
    public ResponseEntity<BasicMessageResponse> deleteSchedule(@PathVariable int scheduleId){
        return adminService.deleteSchedule(scheduleId);
    }

    @PatchMapping("/schedule/{scheduleId}")
    public ResponseEntity<BasicMessageResponse> updateSchedule(@PathVariable int scheduleId, @RequestBody ScheduleDto scheduleDto){
        return adminService.updateSchedule(scheduleId, scheduleDto);
    }
}