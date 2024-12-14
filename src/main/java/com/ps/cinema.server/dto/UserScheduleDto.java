package com.ps.cinema.server.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
public class UserScheduleDto {

    @NotNull
    private int viewer_id;

    @NotNull
    private int schdule_id;
}
