package com.example.boilerroom_labb1.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;


    public SecurityConfig(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/member").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/books").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/books/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/author/{authorId}/books").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/books").hasAnyRole("ADMIN", "LIBRARIAN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/books/edit/{id}").hasAnyRole("ADMIN", "LIBRARIAN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/author").hasAnyRole("ADMIN", "LIBRARIAN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/loans").hasAnyRole("ADMIN", "LIBRARIAN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/loans/{id}").hasAnyRole("ADMIN", "LIBRARIAN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/loans/history").hasAnyRole("ADMIN", "LIBRARIAN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/member/admin").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")));
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();


    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
