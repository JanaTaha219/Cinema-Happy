package com.ps.cinema.server.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

@Entity()
@NoArgsConstructor
@DiscriminatorValue("ROLE_ADMIN")
public class Admin  extends User {
    public Admin(@NotNull(message = "unique name must not be null") @NotEmpty(message = "unique name must not be null") String username, @NotNull(message = "Email must not be null") @NotEmpty(message = "Email must not be empty") @Email(message = "user email must have email format") String email, @Size(min = 8, message = "Password must be at least 8 chars") @NotNull(message = "Password must not be null") @NotEmpty(message = "Password must not be empty") String password) {
        super(username, email, password);
    }
}
