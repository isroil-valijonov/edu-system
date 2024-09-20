package com.example.edusystem.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        registry -> registry
                                .requestMatchers("/user/auth/sign-up", "/user/auth/sign-in", "/user/auth/create-student")
                                .permitAll()
                                .requestMatchers("/swagger-ui.html", "/v3/api-docs/**","/swagger-ui/**")
                                .permitAll()
                                .requestMatchers("/user/auth/create-admin").hasRole("MANAGEMENT")
                                .requestMatchers("/user/auth/create-teacher").hasRole("ADMIN")
                                .requestMatchers("/management/**").permitAll()
                                .requestMatchers("/admin/**").permitAll()
                                .requestMatchers("/teacher/**").permitAll()
                                .requestMatchers("/student/**").permitAll()
                                .requestMatchers("/group/**").permitAll()
                                .requestMatchers("/attendance/**").permitAll()
                                .requestMatchers("/comment/**").permitAll()
                                .requestMatchers("/course/**").permitAll()
                                .requestMatchers("/exam/**").permitAll()
                                .requestMatchers("/exam-results/**").permitAll()
                                .requestMatchers("/rating/**").permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

