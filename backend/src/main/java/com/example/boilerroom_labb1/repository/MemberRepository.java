package com.example.boilerroom_labb1.repository;

import com.example.boilerroom_labb1.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
