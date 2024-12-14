package com.ps.cinema.server.model;

import com.ps.cinema.server.repository.UserRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
@ToString
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@Component
@DiscriminatorColumn(name = "role")
public class User implements Serializable {
    @Id
    @NotNull(message= "user id must not be null")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    @NotNull(message= "unique name must not be null")
    @NotEmpty(message= "unique name must not be null")
    private String username;

    @NotNull(message= "Email must not be null")
    @NotEmpty(message= "Email must not be empty")
    @Email(message= "user email must have email format")
    @Column(unique = true)
    private String email;

    @Size(min = 8, message= "Password must be at least 8 chars")
    @NotNull(message= "Password must not be null")
    @NotEmpty(message= "Password must not be empty")
    private String password;




    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

}