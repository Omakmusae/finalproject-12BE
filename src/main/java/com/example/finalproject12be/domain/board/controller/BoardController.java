package com.example.finalproject12be.domain.board.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.finalproject12be.domain.board.dto.BoardRequest;

import com.example.finalproject12be.domain.board.dto.BoardResponse;
import com.example.finalproject12be.domain.board.entity.Board;
import com.example.finalproject12be.domain.board.service.BoardService;
import com.example.finalproject12be.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BoardController {

	private final BoardService boardService;

	@GetMapping("/api/board")
	public ResponseEntity<Page<BoardResponse>> getBoardList(
		@RequestParam("page") int page, @RequestParam("size") int size
	) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(boardService.getAllBoards(page, size));
	}

	@GetMapping("/api/board/{boardId}")
	public ResponseEntity<BoardResponse> getBoard(@PathVariable final Long boardId) {
		System.out.println(boardId + " !!!!!!!!!!!!");
		return ResponseEntity.status(HttpStatus.OK)
			.body(boardService.getBoard(boardId));
	}

	@PostMapping("/api/board")
	public ResponseEntity<Void> createBoard(
		@AuthenticationPrincipal final UserDetailsImpl userDetails,
		@RequestBody final BoardRequest boardRequest) {

		System.out.println(boardRequest.getContent());
		System.out.println(boardRequest.getTitle());
		boardService.createBoard(userDetails.getMember(), boardRequest);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@PutMapping("/api/board/{boardId}")
	public ResponseEntity<Void> updateBoard(
		@AuthenticationPrincipal final UserDetailsImpl userDetails,
		@PathVariable final Long boardId,
		@RequestBody final BoardRequest boardRequest) {

		boardService.updateBoard(userDetails.getMember(), boardId, boardRequest);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@DeleteMapping("/api/board/{boardId}")
	public ResponseEntity<Void> deleteBoard(
		@AuthenticationPrincipal final UserDetailsImpl userDetails,
		@PathVariable final Long boardId) {

		boardService.deleteBoard(userDetails.getMember(), boardId);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

}
