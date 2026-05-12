package com.example.boilerroom_labb1.mapper;


import com.example.boilerroom_labb1.dto.member.MemberRequestDto;
import com.example.boilerroom_labb1.dto.member.MemberResponseDto;
import com.example.boilerroom_labb1.entity.member.Member;
import com.example.boilerroom_labb1.entity.member.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    private final PasswordEncoder passwordEncoder;

    public MemberMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public MemberResponseDto toResponse(Member member) {
        return new MemberResponseDto(member.getName(), member.getPhone(), member.getPersonId(), member.getEmail());
    }

    public Member toMemberEntity(MemberRequestDto request, Role role) {
        Member member = new Member();
        member.setName(request.name());
        member.setPhone(request.phone());
        member.setPersonId(request.personId());
        member.setEmail(request.email());
        member.setPassword(passwordEncoder.encode(request.password()));
        member.setRole(role);
        return member;
    }
}
