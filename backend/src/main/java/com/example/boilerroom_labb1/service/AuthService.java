package com.example.boilerroom_labb1.service;

import com.example.boilerroom_labb1.dto.auth.LoginRequestDTO;
import com.example.boilerroom_labb1.dto.auth.LoginResponseDTO;
import com.example.boilerroom_labb1.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

}
