package com.ps.cinema.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
public class Schedule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Positive(message = "schedule price must be positive")
    @NotNull(message = "schedule price must not null")
    private int price;

    @ManyToOne()
    @JoinColumn(name = "film_id", referencedColumnName = "id")
    @JsonBackReference
    @NotNull(message = "film must be not null")
    private Film film;

    @PositiveOrZero(message = "number of available seats must be positive or zero")
    @NotNull(message = "available seats must be not null")
    private int availableSeats;

    @FutureOrPresent(message = "show date must be in future")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "show date must be not null")
    private Date showDateTime;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<UserSchedule> schedules;

    public Schedule(int price, Film film, int availableSeats, Date showDateTime) {
        this.price = price;
        this.film = film;
        this.availableSeats = availableSeats;
        this.showDateTime = showDateTime;
    }
}
