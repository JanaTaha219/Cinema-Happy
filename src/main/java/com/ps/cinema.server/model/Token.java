package com.ps.cinema.server.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token implements Serializable {
    @Id
    private String id;
    private boolean blacklisted;

    public Token(String id) {
        this.id = id;
        this.blacklisted = false;
    }

    public boolean isBlacklisted() {
        return blacklisted;
    }
}
