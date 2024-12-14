package com.ps.cinema.server.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ps.cinema.server.model.Producer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class FilmDto {

    @NotNull
    private String name;

    @Nullable

    private int producerId;

    @Transient
    private double totalRating;

}
