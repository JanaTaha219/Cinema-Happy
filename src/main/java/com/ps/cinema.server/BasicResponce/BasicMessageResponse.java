package com.ps.cinema.server.BasicResponce;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BasicMessageResponse {
    private Map<String, Object> data = new HashMap<>();
    int status;

    public BasicMessageResponse(String key, Object value, int status) {
        data.put(key, value);
        this.status = status;
    }
}
