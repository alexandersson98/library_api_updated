package com.example.boilerroom_labb1.dto.member;


import com.example.boilerroom_labb1.entity.member.Role;

public record MemberRequestDto(String name,
                               String phone,
                               String personId,
                               String email,
                               String password
){}
