package com.ps.cinema.server.controller;

import com.ps.cinema.server.BasicResponce.BasicMessageResponse;
import com.ps.cinema.server.dto.CinemaUserDto;
import com.ps.cinema.server.exception.GeneralException;
import com.ps.cinema.server.mapper.EntityDtoConverter;
import com.ps.cinema.server.model.Role;
import com.ps.cinema.server.model.SecurityUser;
import com.ps.cinema.server.model.Token;
import com.ps.cinema.server.model.User;
import com.ps.cinema.server.repository.UserRepository;
import com.ps.cinema.server.service.AdminService;
import com.ps.cinema.server.service.AuthenticationService;
import com.ps.cinema.server.service.CinemaAppUserService;
import com.ps.cinema.server.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Objects;

@RestController
//@RequestMapping("/api/v1")
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    EntityDtoConverter converter;
    @Autowired
    JwtService jwtService;
    @Autowired
    CinemaAppUserService cinemaAppUserService;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/authenticate")
    public ResponseEntity<BasicMessageResponse> authenticate(@RequestBody CinemaUserDto user){
        return authenticationService.authenticate(user);
    }

    @PostMapping("/signup")
    public ResponseEntity<BasicMessageResponse> addUser(@RequestBody CinemaUserDto user){
        return authenticationService.signUp(user);
    }

//    @PostMapping("/token")
//    public Token getToken(@RequestBody Map<String, String> x){
//        return authenticationService.getTokenn(x.get("token"));
//
//    }

    @GetMapping ("/out")
    public ResponseEntity<BasicMessageResponse> logout(@RequestHeader("Authorization") String token){
        return authenticationService.logout(token);
    }

    @GetMapping("/signupp")
    public ModelAndView showSignupForm() {
        ModelAndView model = new ModelAndView("signup.html");
        model.addObject("user", new User());
        return model;
    }

    @PostMapping("/signupp")
    public ModelAndView processSignup(User user) {
        ModelAndView model = new ModelAndView();
        try {
            ResponseEntity<BasicMessageResponse> x =  authenticationService.signUp(converter.convertToDto(user, CinemaUserDto.class));
            String token = (String) x.getBody().getData().get("token");
            String username = jwtService.extractUsername(token);
            User user1 = cinemaAppUserService.getCinemaUserByUsername(username);
            int id = user1.getId();
            model.addObject("token", token);
            model.addObject("username", username);
            model.addObject("id", id);
            model.setViewName("viewer.html");
            return model;
        } catch (Exception e){
            model.addObject("errorMessage", "Invalid credentials " + e.getMessage());
            model.setViewName("signup.html");
            return model;
        }
    }

    @GetMapping("/login")
    public ModelAndView showLoginForm() {
        ModelAndView model = new ModelAndView("authenticate.html");
        model.addObject("CinemaUserDto", new CinemaUserDto());
        return model;
    }

    @PostMapping("/authenticatee")
    public ModelAndView processLogin(CinemaUserDto user) {
        ModelAndView model = new ModelAndView();
        try {
            ResponseEntity<BasicMessageResponse> x = authenticationService.authenticate(converter.convertToDto(user, CinemaUserDto.class));
            String token = (String) x.getBody().getData().get("token");
            String username = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);
            User user1 = cinemaAppUserService.getCinemaUserByUsername(username);
            SecurityUser securityUser = new SecurityUser(user1, userRepository);
            int id = user1.getId();
            model.addObject("token", token);
            model.addObject("username", username);
            model.addObject("id", id);
            model.addObject("securityUser", securityUser);
            //based on token return the page.
            if(Objects.equals(role, Role.ROLE_PRODUCER.toString()))
                model.setViewName("producer.html");
            else if(Objects.equals(role, Role.ROLE_VIEWER.toString()))
                model.setViewName("viewer.html");
            else if (Objects.equals(role, Role.ROLE_ADMIN.toString()))
                model.setViewName("admin.html");
            else {
                throw new GeneralException("Unauthorized");
            }

            return model;
        } catch (Exception e) {
            model.addObject("errorMessage", "Invalid credentials");
            model.setViewName("authenticate.html");
            return model;
        }
    }
}