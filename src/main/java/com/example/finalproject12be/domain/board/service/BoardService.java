package com.example.finalproject12be.domain.board.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.finalproject12be.domain.board.dto.BoardRequest;
import com.example.finalproject12be.domain.board.dto.BoardResponse;
import com.example.finalproject12be.domain.board.entity.Board;
import com.example.finalproject12be.domain.board.repository.BoardRepository;
import com.example.finalproject12be.domain.member.entity.Member;
import com.example.finalproject12be.exception.MemberErrorCode;
import com.example.finalproject12be.exception.RestApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardService {

	private final BoardRepository boardRepository;

	@Transactional(readOnly = true)
	public List<BoardResponse> searchBoards() {

		return boardRepository.findAll()
			.stream()
			.map(BoardResponse::from)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public BoardResponse searchBoard(final Long boardId) {

		Board board = findBoardByIdOrElseThrow(boardId);
		boardRepository.findById(boardId).orElseThrow(
			() -> new RestApiException(MemberErrorCode.INACTIVE_MEMBER));

		return BoardResponse.from(board);
	}


	@Transactional
	public void createBoard(final Member member, final BoardRequest boardRequest) {

		boardRequest.setMember(member);

		Board board = boardRepository.saveAndFlush(BoardRequest.toEntity(boardRequest));
	}

	@Transactional
	public void updateBoard(final Member member, final Long boardId, final BoardRequest boardRequest) {

		Board board = findBoardByIdOrElseThrow(boardId);

		throwIfNotOwner(board, member.getNickname());

		board.updateBoard(boardRequest);
	}

	@Transactional
	public void deleteBoard(final Member member, final Long boardId) {

		Board board = findBoardByIdOrElseThrow(boardId);

		boardRepository.delete(board);
	}

	private Board findBoardByIdOrElseThrow(Long boardId) {

		return boardRepository.findById(boardId).orElseThrow(
			() -> new RestApiException(MemberErrorCode.INACTIVE_MEMBER)
		);
	}

	private void throwIfNotOwner(Board board, String loginUsername) {

		if (!board.getMember().getNickname().equals(loginUsername))
			throw new RestApiException(MemberErrorCode.INACTIVE_MEMBER);
	}

}