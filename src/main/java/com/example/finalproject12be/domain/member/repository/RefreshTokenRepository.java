package com.example.finalproject12be.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.finalproject12be.domain.member.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByEmail(String email);

	Optional<RefreshToken> deleteRefreshTokenByEmail(String email);

}

