package com.ps.cinema.server;

import com.ps.cinema.server.model.Role;
import com.ps.cinema.server.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@SpringBootApplication
public class CinemaHappyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaHappyApplication.class, args);
//        User user = new User("ii", "email@email.com","password", Role.ROLE_PRODUCER);
//        System.out.println(user.getRole().toString());
    }

}
