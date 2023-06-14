package com.example.finalproject12be.domain.board.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import org.springframework.http.HttpHeaders;
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

import com.example.finalproject12be.domain.board.dto.BoardDetailResponse;
import com.example.finalproject12be.domain.board.dto.BoardRequest;

import com.example.finalproject12be.domain.board.dto.BoardResponse;
import com.example.finalproject12be.domain.board.service.BoardService;
import com.example.finalproject12be.domain.member.entity.MemberRoleEnum;
import com.example.finalproject12be.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BoardController {

	private final BoardService boardService;

	@GetMapping("/api/board")
	public ResponseEntity<Page<BoardResponse>> getBoardList(
		@RequestParam("page") int page, @RequestParam("size") int size,
		@AuthenticationPrincipal final UserDetailsImpl userDetails
	) {

		HttpHeaders headers = new HttpHeaders();

		if (userDetails.getMember().getRole() == MemberRoleEnum.ADMIN) {
			headers.add("adminCheck", "true");
		} else {
			headers.add("adminCheck", "false");
		}

		return ResponseEntity.status(HttpStatus.OK)
			.headers(headers)
			.body(boardService.getAllBoards(page, size));

	}

	@GetMapping("/api/board/{boardId}")
	public ResponseEntity<BoardDetailResponse> getBoard(@PathVariable final Long boardId, @AuthenticationPrincipal final UserDetailsImpl userDetails) {
		HttpHeaders headers = new HttpHeaders();

		if (userDetails.getMember().getRole() == MemberRoleEnum.ADMIN) {
			headers.add("adminCheck", "true");
		} else {
			headers.add("adminCheck", "false");
		}
		return ResponseEntity.status(HttpStatus.OK)
			.headers(headers)
			.body(boardService.getBoard(boardId));
	}

	@PostMapping("/api/board")
	public ResponseEntity<String> createBoard(
		@AuthenticationPrincipal final UserDetailsImpl userDetails,
		@RequestBody final BoardRequest boardRequest) {

		System.out.println(boardRequest.getContent());
		System.out.println(boardRequest.getTitle());
		boardService.createBoard(userDetails.getMember(), boardRequest);
		//return ResponseEntity.status(HttpStatus.OK).body(null);
		return ResponseEntity.ok("공지사항이 등록되었습니다.");
	}

	@PutMapping("/api/board/{boardId}")
	public ResponseEntity<String> updateBoard(
		@AuthenticationPrincipal final UserDetailsImpl userDetails,
		@PathVariable final Long boardId,
		@RequestBody final BoardRequest boardRequest) {

		boardService.updateBoard(userDetails.getMember(), boardId, boardRequest);
		//return ResponseEntity.status(HttpStatus.OK).body(null);
		return ResponseEntity.ok("공지사항이 수정되었습니다.");
	}

	@DeleteMapping("/api/board/{boardId}")
	public ResponseEntity<String> deleteBoard(
		@AuthenticationPrincipal final UserDetailsImpl userDetails,
		@PathVariable final Long boardId) {

		boardService.deleteBoard(userDetails.getMember(), boardId);
		//return ResponseEntity.status(HttpStatus.OK).body(null);
		return ResponseEntity.ok("공지사항이 삭제되었습니다.");
	}

}
