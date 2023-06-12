package com.example.finalproject12be.domain.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.finalproject12be.domain.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

	@Override
	Optional<Board> findById(Long id);

	@Override
	Page<Board> findAll(Pageable pageable);

}
