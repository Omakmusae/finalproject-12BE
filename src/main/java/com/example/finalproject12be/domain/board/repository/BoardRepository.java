package com.example.finalproject12be.domain.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.finalproject12be.domain.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

	@Override
	Optional<Board> findById(Long id);

	@Override
	Page<Board> findAll(Pageable pageable);

	@Query(value = "SELECT * FROM `board` WHERE board_id = (SELECT prev_no FROM (SELECT board_id, LAG(board_id, 1, -1) OVER (ORDER BY board_id) AS prev_no FROM `board`) AS subquery WHERE board_id = :boardId)", nativeQuery = true)
	Optional<Board> findPrevBoard(@Param("boardId") Long boardId);

	@Query(value = "SELECT * FROM `board` WHERE board_id = (SELECT prev_no FROM (SELECT board_id, LEAD(board_id, 1, -1) OVER (ORDER BY board_id) AS prev_no FROM `board`) AS subquery WHERE board_id = :boardId)", nativeQuery = true)
	Optional<Board> findNextBoard(@Param("boardId") Long id);

}