package com.ps.cinema.server.filter;

import com.ps.cinema.server.model.User;
import com.ps.cinema.server.service.CinemaAppUserService;
import com.ps.cinema.server.service.JwtService;
import com.ps.cinema.server.service.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    CinemaAppUserService cinemaAppUserService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        //if the token is valid -> extract the token
        String token = authHeader.substring(7);//skip Bearer text with the space
        //Extract username using JwtService
        String username = jwtService.extractUsername(token);
        //now we need to authenticate the user if he is not authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null && cinemaAppUserService.isUserExist(username)) {

            UserDetails user = userDetailsService.loadUserByUsername(username);

            User userX = cinemaAppUserService.getCinemaUserByUsername(username);


            if (jwtService.isValid(token, userX)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
