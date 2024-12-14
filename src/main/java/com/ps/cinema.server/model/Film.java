package com.ps.cinema.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter @Getter @ToString
@AllArgsConstructor @NoArgsConstructor
public class Film {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private int id;

    @NotNull
    private String name;

    @Transient
    private double totalRating;

    @ManyToOne()
    @JoinColumn(name = "producer_id", referencedColumnName = "id")
    @JsonBackReference
    private Producer producer;

    @JsonIgnore
    @OneToMany(mappedBy = "film", fetch = FetchType.EAGER)
    private Set<FilmRating> ratings = new HashSet<>();

    @OneToMany(mappedBy = "film", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Schedule> schedules;
}