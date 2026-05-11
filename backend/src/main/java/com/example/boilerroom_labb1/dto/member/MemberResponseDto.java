package com.example.boilerroom_labb1.dto.member;

import com.example.boilerroom_labb1.entity.Loan;
import com.example.boilerroom_labb1.entity.member.Role;

public record MemberResponseDto(String name,
                                String phone,
                                String personId,
                                String email) {
}
