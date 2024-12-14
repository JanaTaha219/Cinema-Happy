package com.ps.cinema.server.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter @Getter @ToString
@NoArgsConstructor @AllArgsConstructor
public class CinemaUserDto {

    private String username;

    private String email;

    private String password;

    public CinemaUserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
