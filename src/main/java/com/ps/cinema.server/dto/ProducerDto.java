package com.ps.cinema.server.dto;

import com.ps.cinema.server.model.Film;
import lombok.*;

import java.util.Set;

@Setter @Getter @ToString
@AllArgsConstructor @NoArgsConstructor
public class ProducerDto {
    private String username;

    private String email;

    private double rating;

    private Set<FilmDto> films;
}
