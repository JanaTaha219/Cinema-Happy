package com.ps.cinema.server.repository;

import com.ps.cinema.server.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    public void deleteAllByFilmId(int filmId);
}
