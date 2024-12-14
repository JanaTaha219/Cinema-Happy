package com.ps.cinema.server.repository;

import com.ps.cinema.server.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FilmRepository extends JpaRepository<Film, Integer> {

    List<Film> findByNameContaining(String name);

    Optional<Film> findByName(String name);

    List<Film> findByProducerId(int producerId);

    List<Film> findByProducerUsername(String username);

    public void deleteAllByProducerId(int producerId);

}
