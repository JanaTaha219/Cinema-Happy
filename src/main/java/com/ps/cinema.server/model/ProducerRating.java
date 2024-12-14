package com.ps.cinema.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Setter @Getter

public class ProducerRating implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int ratingId;

    @ManyToOne()
    @JoinColumn(name = "viewer_id", referencedColumnName = "id")
    @JsonBackReference
    private User viewer;

    @ManyToOne()
    @JoinColumn(name = "producer_id", referencedColumnName = "id")
    @JsonBackReference
    private User producer;


    @Min(0) @Max(5)
    private int rating;

    public ProducerRating(Viewer viewer, Producer producer, int rating) {
        this.viewer = viewer;
        this.producer = producer;
        this.rating = rating;
    }
}
