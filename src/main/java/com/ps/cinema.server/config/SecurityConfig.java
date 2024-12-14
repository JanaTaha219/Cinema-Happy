package com.ps.cinema.server.config;

import com.ps.cinema.server.filter.JwtAuthenticationFilter;
import com.ps.cinema.server.model.Role;
import com.ps.cinema.server.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${accessible.admin.role}")
    private String accessibleAdminRole;

    @Value("${accessible.producer.role}")
    private String accessibleProducerRole;

    @Value("${accessible.viewer.role}")
    private String accessibleViewerRole;

    @Value("${accessible.post.endpoints}")
    private String accessiblePostEndpoints;

    @Value("${accessible.get.endpoints}")
    private String accessibleGetEndpoints;

    @Value("${accessible.registered.get.endpoints}")
    private String accessibleRegisteredGetEndpoints;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        req ->  req
                                .requestMatchers(HttpMethod.GET,"/v3/**", "/swagger-ui/**").permitAll()
                                .requestMatchers(accessibleRegisteredGetEndpoints.split(",")).hasAnyAuthority(Role.ROLE_ADMIN.toString(), Role.ROLE_PRODUCER.toString(), Role.ROLE_VIEWER.toString())
                                .requestMatchers(accessibleViewerRole.split(",")).hasAnyAuthority(Role.ROLE_ADMIN.toString(),  Role.ROLE_VIEWER.toString())
                                .requestMatchers(accessibleProducerRole.split(",")).hasAnyAuthority(Role.ROLE_ADMIN.toString() ,  Role.ROLE_PRODUCER.toString())
                                .requestMatchers(accessibleAdminRole.split(",")).hasAuthority(Role.ROLE_ADMIN.toString())
                                .requestMatchers(HttpMethod.POST,  accessiblePostEndpoints.split(",")).permitAll()
                                .requestMatchers(HttpMethod.GET, accessibleGetEndpoints.split(",")).permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .httpBasic(withDefaults())
                .formLogin(
                        login -> login
                                .loginPage("/login")
                                .permitAll()
                                .failureUrl("/login?error=true")
                )
                .userDetailsService(userDetailsService)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        //return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
