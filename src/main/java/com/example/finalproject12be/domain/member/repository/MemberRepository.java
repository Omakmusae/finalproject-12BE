package com.example.finalproject12be.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.finalproject12be.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository <Member, Long> {
	Optional<Member> findByEmail(String email);
	Optional<Member> findByNickname(String nickname);

	Optional<Member> findByKakaoId(Long id);
}
