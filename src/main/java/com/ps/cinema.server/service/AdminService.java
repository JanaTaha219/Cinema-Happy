package com.ps.cinema.server.service;

import com.ps.cinema.server.BasicResponce.BasicMessageResponse;
import com.ps.cinema.server.dto.ScheduleDto;
import com.ps.cinema.server.model.SecurityUser;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AdminService {
    public ResponseEntity<BasicMessageResponse> addSchedule(ScheduleDto scheduleDto);
    public ResponseEntity<BasicMessageResponse> deleteAdmin(String idType, String id);
    public ResponseEntity<BasicMessageResponse> deleteSchedule(int scheduleId);

    public boolean isAdmin(String id, String idType);

    public ResponseEntity<BasicMessageResponse> updateSchedule(int scheduleId, ScheduleDto scheduleDto);

    public ResponseEntity<BasicMessageResponse> getAdminByIdR(int id);

    public ResponseEntity<BasicMessageResponse> getAdminByUsernameR(String username);

    public ResponseEntity<BasicMessageResponse> getAdminByEmailR(String email);

    public ResponseEntity<BasicMessageResponse> getAllAdmins();
}
