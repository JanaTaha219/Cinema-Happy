package com.ps.cinema.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FilmRating {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int ratingId;

    @ManyToOne()
    @JoinColumn(name = "viewer_id", referencedColumnName = "id")
    @JsonBackReference
    private Viewer viewer;

    @ManyToOne()
    @JoinColumn(name = "Film_id", referencedColumnName = "id")
    @JsonBackReference
    private Film film;


    @Max(5) @Min(0)
    private int rating;

    public FilmRating(Viewer viewer, Film film, int rating) {
        this.viewer = viewer;
        this.film = film;
        this.rating = rating;
    }
}
