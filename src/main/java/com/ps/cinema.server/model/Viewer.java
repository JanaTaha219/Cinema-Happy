package com.ps.cinema.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("ROLE_VIEWER")
public class Viewer extends User {

    @JsonBackReference
    @OneToMany(mappedBy = "viewer", fetch = FetchType.EAGER)
    private Set<ProducerRating> producerRatings = new HashSet<>();

    @JsonBackReference
    @OneToMany(mappedBy = "viewer", fetch = FetchType.EAGER)
    private Set<FilmRating> filmRatings = new HashSet<>();

    @JsonBackReference
    @OneToMany(mappedBy = "viewer", fetch = FetchType.EAGER)
    private Set<UserSchedule> schedules;

}
