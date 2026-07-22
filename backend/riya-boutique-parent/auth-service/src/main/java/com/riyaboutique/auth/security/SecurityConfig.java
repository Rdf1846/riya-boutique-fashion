package com.riyaboutique.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /*
     * Spring will inject our custom UserDetailsService.
     * AuthenticationProvider will use this service
     * to load user details from the database.
     */
    private final CustomUserDetailsServiceImpl customUserDetailsService;

    public SecurityConfig(CustomUserDetailsServiceImpl customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    /*
     * Registers BCryptPasswordEncoder as a Spring Bean.
     *
     * Whenever Spring needs a PasswordEncoder,
     * it will provide BCryptPasswordEncoder.
     *
     * Used during:
     * 1. Signup -> password encoding
     * 2. Login  -> password verification using matches()
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * Registers DaoAuthenticationProvider as a Spring Bean.
     *
     * DaoAuthenticationProvider is responsible for:
     * - Loading user from database
     * - Comparing passwords
     * - Deciding authentication success/failure
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        // Tell Spring where users are stored
        provider.setUserDetailsService(customUserDetailsService);

        // Tell Spring which password encoder to use
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    /*
     * Registers AuthenticationManager Bean.
     *
     * AuthenticationManager coordinates the complete
     * authentication process.
     *
     * NOTE:
     * We are NOT authenticating users here.
     * We are only exposing AuthenticationManager
     * as a Spring Bean.
     *
     * During login we will call:
     *
     * authenticationManager.authenticate(...)
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    /*
     * Main Security Configuration.
     *
     * Every incoming HTTP request passes through
     * this Security Filter Chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http

                /*
                 * JWT is Stateless.
                 * Therefore CSRF protection is not required.
                 */
                .csrf(csrf -> csrf.disable())

                /*
                 * Define which endpoints are public
                 * and which require authentication.
                 */
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/v1/users/signup",
                                "/api/v1/users/login",
                                "/api/v1/users/welcome"
                        ).permitAll()

                        .anyRequest().authenticated()
                )

                /*
                 * We are using JWT.
                 *
                 * Therefore Spring must NOT create
                 * HttpSession.
                 */
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                /*
                 * Register our AuthenticationProvider.
                 */
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

}