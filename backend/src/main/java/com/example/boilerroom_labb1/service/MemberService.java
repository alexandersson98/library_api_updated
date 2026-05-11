package com.example.boilerroom_labb1.service;

import com.example.boilerroom_labb1.dto.member.MemberRequestDto;
import com.example.boilerroom_labb1.dto.member.MemberResponseDto;
import com.example.boilerroom_labb1.entity.member.Member;
import com.example.boilerroom_labb1.mapper.MemberMapper;
import com.example.boilerroom_labb1.repository.MemberRepository;
import org.springframework.http.ResponseEntity;
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
        Member member = memberMapper.toMemberEntity(memberRequestDto);
        memberRepository.save(member);
        return memberMapper.toResponse(member);

    }
}
