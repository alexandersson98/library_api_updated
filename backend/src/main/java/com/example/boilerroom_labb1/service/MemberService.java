package com.example.boilerroom_labb1.service;

import com.example.boilerroom_labb1.dto.member.MemberRequestDto;
import com.example.boilerroom_labb1.dto.member.MemberResponseDto;
import com.example.boilerroom_labb1.entity.member.Member;
import com.example.boilerroom_labb1.entity.member.Role;
import com.example.boilerroom_labb1.mapper.MemberMapper;
import com.example.boilerroom_labb1.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;

    public MemberService(MemberMapper memberMapper, MemberRepository memberRepository) {
        this.memberMapper = memberMapper;
        this.memberRepository = memberRepository;
    }

    public MemberResponseDto createMember(MemberRequestDto memberRequestDto){
        Member member = memberMapper.toMemberEntity(memberRequestDto, Role.USER);
        memberRepository.save(member);
        return memberMapper.toResponse(member);
    }

    public MemberResponseDto createAdmin(MemberRequestDto memberRequestDto){
        Member member = memberMapper.toMemberEntity(memberRequestDto, Role.ADMIN);
        memberRepository.save(member);
        return memberMapper.toResponse(member);
    }

    public MemberResponseDto createLibrarian(MemberRequestDto memberRequestDto){
        Member member = memberMapper.toMemberEntity(memberRequestDto, Role.LIBRARIAN);
        memberRepository.save(member);
        return memberMapper.toResponse(member);
    }
}
