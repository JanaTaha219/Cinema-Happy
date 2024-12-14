package com.ps.cinema.server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
public class UserSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "viewer_id", referencedColumnName = "id")
    private User viewer;

    @ManyToOne
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    public UserSchedule(Viewer viewer, Schedule schedule) {
        this.viewer = viewer;
        this.schedule = schedule;
    }
}
