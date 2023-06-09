package com.example.finalproject12be.domain.validNumber.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.finalproject12be.domain.validNumber.entity.ValidNumber;

public interface ValidNumberRepository extends JpaRepository<ValidNumber, Long> {
	Optional<List<ValidNumber>> findAllByEmail(String email);

	Optional<ValidNumber> findByEmail(String email);
}
