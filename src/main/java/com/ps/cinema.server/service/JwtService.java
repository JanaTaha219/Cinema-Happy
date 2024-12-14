package com.ps.cinema.server.service;

import com.ps.cinema.server.model.User;
import com.ps.cinema.server.model.Token;
import com.ps.cinema.server.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${secret.key}")
    private String secretKey;

    @Resource(name="redisTemplate")
    private HashOperations<String, String, Token> hashOperations;

    @Autowired
    UserRepository userRepository;



    @Value("${session.time}")
    private long sessionTime;

    public Token getToken(String key){
        return hashOperations.get("Token", key);
    }
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token){return extractClaim(token, claims -> claims.get("roles", String.class));}

    public boolean isValid(String token, User user){
        String username = extractUsername(token);
        boolean isLoggedOut = getToken(token).isBlacklisted();
        boolean tokenExist = getToken(token)!=null;
        return (user.getUsername().equals(username) && !isTokenExpire(token) && !isLoggedOut && tokenExist);
    }

    public boolean isTokenExpire(String token) {
        return extractExpirationTime(token).before(new Date());
    }

    private Date extractExpirationTime(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(User user){
        String role = userRepository.getRoleById(user.getId())!=null?userRepository.getRoleById(user.getId()):"ROLE_VIEWER";
        return Jwts
                .builder()
                .subject(user.getUsername())
                .claim("roles", role)
                .issuedAt(new Date(System.currentTimeMillis()))  //Unique Mon Apr 15 12:10:55 IDT 2024
                .expiration(new Date(System.currentTimeMillis() + sessionTime))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
