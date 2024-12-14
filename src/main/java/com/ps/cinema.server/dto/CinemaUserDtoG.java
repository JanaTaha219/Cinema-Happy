package com.ps.cinema.server.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter @Getter @ToString
@NoArgsConstructor @AllArgsConstructor
public class CinemaUserDtoG {

    private int id;

    private String username;

    private String email;

}
