package com.raqaf.ecom.config;

import com.raqaf.ecom.security.CustomUserDetailsService;
import com.raqaf.ecom.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * CONCEPT: Full JWT-based Spring Security setup.
 *  - Stateless sessions (SessionCreationPolicy.STATELESS) - no server-side
 *    session storage, everything derived from the JWT on each request
 *  - Public endpoints: /api/auth/** (register/login), GET on products/categories
 *  - Everything else requires authentication
 *  - Admin-only writes are enforced with hasRole("ADMIN") on specific paths
 *  - JwtAuthFilter runs BEFORE Spring's own UsernamePasswordAuthenticationFilter
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public: register/login, browsing the catalog, H2 console, Swagger
                        .requestMatchers("/api/auth/**", "/h2-console/**",
                                "/swagger-ui/**", "/api-docs/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET,
                                "/api/products/**", "/api/categories/**").permitAll()
                        // Admin-only writes
                        .requestMatchers(org.springframework.http.HttpMethod.POST,
                                "/api/products/**", "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT,
                                "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE,
                                "/api/products/**").hasRole("ADMIN")
                        // Everything else needs a valid JWT
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Needed so the H2 console (which uses frames) still renders
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        // Pass the UserDetailsService bean
        provider.setUserDetailsService(userDetailsService);

        // Pass the PasswordEncoder bean (by calling the bean method below)
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
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