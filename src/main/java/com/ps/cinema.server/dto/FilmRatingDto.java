package com.ps.cinema.server.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
public class FilmRatingDto {

    @NotNull
    private int viewerId;

    @NotNull
    private int filmId;

    @NotNull
    private int rating;
}
