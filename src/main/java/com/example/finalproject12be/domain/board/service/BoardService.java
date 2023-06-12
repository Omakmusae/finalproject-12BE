package com.example.finalproject12be.domain.board.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.finalproject12be.domain.board.dto.BoardRequest;
import com.example.finalproject12be.domain.board.dto.BoardResponse;
import com.example.finalproject12be.domain.board.dto.MappedBoardRequest;
import com.example.finalproject12be.domain.board.entity.Board;
import com.example.finalproject12be.domain.board.repository.BoardRepository;
import com.example.finalproject12be.domain.member.entity.Member;
import com.example.finalproject12be.domain.member.entity.MemberRoleEnum;
import com.example.finalproject12be.exception.MemberErrorCode;
import com.example.finalproject12be.exception.RestApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardService {

	private final BoardRepository boardRepository;

	@Transactional(readOnly = true)
	public Page<BoardResponse> getAllBoards(int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
		Page<Board> boardsPage = boardRepository.findAll(pageable);

		List<BoardResponse> boardList = boardsPage.getContent().stream()
			.map(BoardResponse::new)
			.collect(Collectors.toList());

		return new PageImpl<>(boardList, pageable, boardsPage.getTotalElements());

	}

	@Transactional(readOnly = true)
	public BoardResponse getBoard(final Long boardId) {

		Board board = findBoardByIdOrElseThrow(boardId);
		boardRepository.findById(boardId).orElseThrow(
			() -> new RestApiException(MemberErrorCode.BOARD_NOT_FOUND));

		return new BoardResponse(boardId, board.getTitle(), board.getContent(), board.getMember().getNickname());
	}

	@Transactional
	public void createBoard(final Member member, final BoardRequest boardRequest) {

		MemberRoleEnum memberRoleEnum =  member.getRole();
		if (memberRoleEnum != MemberRoleEnum.ADMIN) {
			throw new RestApiException(MemberErrorCode.ADMIN_ERROR);
		}
		MappedBoardRequest board = new MappedBoardRequest(boardRequest.getTitle(), boardRequest.getContent(), member);
		Board result = boardRepository.saveAndFlush(MappedBoardRequest.toEntity(board));

	}

	@Transactional
	public void updateBoard(final Member member, final Long boardId, final BoardRequest boardRequest) {
		MemberRoleEnum memberRoleEnum =  member.getRole();
		if (memberRoleEnum != MemberRoleEnum.ADMIN) {
			throw new RestApiException(MemberErrorCode.ADMIN_ERROR);
		}
		Board board = findBoardByIdOrElseThrow(boardId);

		board.updateBoard(boardRequest);
	}

	@Transactional
	public void deleteBoard(final Member member, final Long boardId) {
		MemberRoleEnum memberRoleEnum =  member.getRole();
		if (memberRoleEnum != MemberRoleEnum.ADMIN) {
			throw new RestApiException(MemberErrorCode.ADMIN_ERROR);
		}
		Board board = findBoardByIdOrElseThrow(boardId);

		boardRepository.delete(board);
	}

	private Board findBoardByIdOrElseThrow(Long boardId) {

		return boardRepository.findById(boardId).orElseThrow(
			() -> new RestApiException(MemberErrorCode.BOARD_NOT_FOUND)
		);
	}



}