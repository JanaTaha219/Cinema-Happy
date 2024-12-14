package com.ps.cinema.server.service;

import com.ps.cinema.server.repository.UserRepository;
import com.ps.cinema.server.model.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findCinemaAppUserByUsername(username)
                .map(user -> new SecurityUser(user, userRepository))
                .orElseThrow(() -> new UsernameNotFoundException("User name not found: " + username));
    }
}