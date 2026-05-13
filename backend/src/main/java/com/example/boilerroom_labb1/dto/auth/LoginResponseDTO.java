package com.example.boilerroom_labb1.dto.auth;

import com.example.boilerroom_labb1.entity.member.Role;

public record LoginResponseDTO(String token, Role role) {
}
