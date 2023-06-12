package com.example.finalproject12be.domain.profile.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.finalproject12be.domain.profile.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
	Optional<Profile> findByMemberId(Long memberId);
}
