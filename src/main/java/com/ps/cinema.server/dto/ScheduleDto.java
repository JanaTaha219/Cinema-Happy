package com.ps.cinema.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;


@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
public class ScheduleDto {
    @Positive
    private int price;

    private int filmId;

    @PositiveOrZero
    private int availableSeats;

    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date showDateTime;
}
