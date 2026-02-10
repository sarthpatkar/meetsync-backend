package com.meetingscheduler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {}) // ✅ enable CORS from CorsConfig
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ✅ stateless

            .authorizeHttpRequests(auth -> auth
                // ✅ Public endpoints
                .requestMatchers("/api/auth/login", "/api/users/register").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // ✅ Meetings
                .requestMatchers(HttpMethod.GET, "/api/meetings/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/meetings/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/meetings/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/meetings/**").authenticated()

                // ✅ User management
                .requestMatchers(HttpMethod.PUT, "/api/users/update").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/users/change-password").authenticated()

                // ✅ Everything else must be authenticated
                .anyRequest().authenticated()
            );

        // ✅ Add JWT filter AFTER making sure login/register are excluded
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}