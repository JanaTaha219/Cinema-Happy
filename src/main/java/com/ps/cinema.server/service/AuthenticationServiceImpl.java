package com.ps.cinema.server.service;

import com.ps.cinema.server.BasicResponce.BasicMessageResponse;
import com.ps.cinema.server.dto.CinemaUserDto;
import com.ps.cinema.server.exception.GeneralException;
import com.ps.cinema.server.mapper.EntityDtoConverter;
import com.ps.cinema.server.model.Role;
import com.ps.cinema.server.model.User;
import com.ps.cinema.server.model.Token;
import com.ps.cinema.server.model.Viewer;
import com.ps.cinema.server.repository.UserRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService{

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CinemaAppUserService cinemaAppUserService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityDtoConverter converter;

    @Resource(name="redisTemplate")
    private HashOperations<String, String, Token> hashOperations;

    public Token getToken(String key){
        Token token1 =  hashOperations.get("Token", key);
        if (token1 != null) return token1;
        throw new GeneralException("Token is not in the database.", HttpStatus.NOT_FOUND);
    }

    public String setToken(Token token){
        hashOperations.put("Token", token.getId(), token);
        return "DONE";
    }

    @Override
    public ResponseEntity<BasicMessageResponse> authenticate(CinemaUserDto cinemaUserDto) {
        try{
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(cinemaUserDto.getUsername(), cinemaUserDto.getPassword()));
        User user = cinemaAppUserService.getCinemaUserByUsername(cinemaUserDto.getUsername());
        var jwt = jwtService.generateToken(user);
        setToken(new Token(jwt, false));
        log.info("logged in");
        return new ResponseEntity<>(new BasicMessageResponse("token", jwt, 200), HttpStatus.OK);
        } catch (Exception e){
            log.error("Could log in");
            throw new GeneralException("Could not log in" + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<BasicMessageResponse> signUp(CinemaUserDto cinemaUserDto) {
        try {
            User user = converter.convertToEntity(cinemaUserDto, User.class);
            userRepository.setViewerType(user.getId());
            userRepository.save(converter.convertToEntity(user, Viewer.class));
            var jwt = jwtService.generateToken(user);
            setToken(new Token(jwt, false));
            log.info("Signed up");
            return new ResponseEntity<>(new BasicMessageResponse("token", jwt, 201), HttpStatus.CREATED);
        } catch (Exception e){
            log.error("could not sign up");
            throw new GeneralException("Could not sign up because of: " + e.getMessage());
        }
    }

    public Token getTokenn(String token){
        Token token1 =  getToken(token);
        if (token1!=null) return token1;
        throw new GeneralException("Token is not in the database.", HttpStatus.NOT_FOUND);
    }

    private void invalidateToken(String token) {
        token = token.substring(7);
        setToken(new Token(token, true));
    }

    public void checkIfValidateToken(String token, User user) {
        Token storedToken = getTokenn(token.substring(7));
        if( storedToken != null && token.startsWith("Bearer ") && jwtService.isValid(token.substring(7), user))
            return;
        throw new GeneralException("Token is invalid", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<BasicMessageResponse> logout(String token) {
        try {
            String username = jwtService.extractUsername(token.substring(7));
            User user = cinemaAppUserService.getCinemaUserByUsername(username);
            checkIfValidateToken(token, user);
            invalidateToken(token);
            log.info("Logged out successfully");
            return new ResponseEntity<>(new BasicMessageResponse("message", "logged out successfully", 200), HttpStatus.OK);
        }  catch (Exception e){
            log.error("could not log out");
            throw new GeneralException("Could not log out" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
