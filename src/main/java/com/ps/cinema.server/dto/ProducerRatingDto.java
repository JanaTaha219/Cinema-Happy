package com.ps.cinema.server.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
public class ProducerRatingDto {

    @NotNull
    private int viewerId;

    @NotNull
    private int producerId;

    @NotNull
    private int rating;
}
