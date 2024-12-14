package com.ps.cinema.server.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@DiscriminatorValue("ROLE_PRODUCER")
public class Producer extends User {

    @JsonManagedReference
    @OneToMany(mappedBy = "producer", fetch = FetchType.EAGER)
    private Set<ProducerRating> ratings = new HashSet<>();

    @OneToMany(mappedBy = "producer", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Film> films;

    @Transient
    private double rating;

    public Producer(@NotNull(message = "unique name must not be null") @NotEmpty(message = "unique name must not be null") String username, @NotNull(message = "Email must not be null") @NotEmpty(message = "Email must not be empty") @Email(message = "user email must have email format") String email, @Size(min = 8, message = "Password must be at least 8 chars") @NotNull(message = "Password must not be null") @NotEmpty(message = "Password must not be empty") String password) {
        super(username, email, password);
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
