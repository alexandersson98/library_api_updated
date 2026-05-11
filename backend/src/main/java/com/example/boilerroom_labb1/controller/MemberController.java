package com.example.boilerroom_labb1.controller;


import com.example.boilerroom_labb1.dto.member.MemberRequestDto;
import com.example.boilerroom_labb1.dto.member.MemberResponseDto;
import com.example.boilerroom_labb1.entity.member.Role;
import com.example.boilerroom_labb1.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController( MemberService memberService){
        this.memberService = memberService;
    }


    @PostMapping
    public ResponseEntity<MemberResponseDto>createMember(@RequestBody MemberRequestDto memberRequestDto){
        MemberResponseDto response =  memberService.createMember(memberRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);


    }



}
