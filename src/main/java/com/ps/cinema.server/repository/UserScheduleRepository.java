package com.ps.cinema.server.repository;

import com.ps.cinema.server.model.UserSchedule;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserScheduleRepository extends JpaRepository<UserSchedule, Integer> {
    @Transactional
    public void deleteAllByViewerId(int viewerId);

    @Transactional
    public void deleteAllByScheduleId(int scheduleId);
}
