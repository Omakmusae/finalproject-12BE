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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardService {

	private final BoardRepository boardRepository;

	@Transactional(readOnly = true)
	public List<BoardResponse> searchBoards() {

		return boardRepository.findAllJoinFetch()
			.stream()
			.map(BoardResponse::from)
			// .sorted(Comparator.comparing(BoardResponse::getCreatedAt).reversed())
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public BoardResponse searchBoard(final Long boardId) {

		Board board = findBoardByIdOrElseThrow(boardId);

		return BoardResponse.from(board);
	}

	@Transactional(readOnly = true)
	public List<BoardResponse> searchBoards(String address) {

		return boardRepository.findByAddressJoinFetch(address)
			.stream()
			.map(BoardResponse::from)
			.collect(Collectors.toList());
	}

	@Transactional
	public void createBoard(final Member member, final BoardRequest boardRequest, final MultipartFile file) {

		boardRequest.setMember(member);

		Board board = boardRepository.saveAndFlush(BoardRequest.toEntity(boardRequest));
	}


	@Transactional
	public void updateBoard(final Member member, final Long boardId, final BoardRequest boardRequest, final MultipartFile file) {

		Board board = findBoardByIdOrElseThrow(boardId);

		throwIfNotOwner(board, member.getUsername());

		board.updateBoard(boardRequest);
	}

	@Transactional
	public void deleteBoard(final Member member, final Long boardId) {

		Board board = findBoardByIdOrElseThrow(boardId);



		boardRepository.delete(board);
	}

}
