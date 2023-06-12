package com.example.finalproject12be.domain.board.dto;

import com.example.finalproject12be.domain.board.entity.Board;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardDetailResponse {
	private Long id;
	private String title;
	private String content;
	private String nickname;
	private Long prevId;
	private Long nextId;

	public BoardDetailResponse(Board board) {
		this.id = board.getId();
		this.title = board.getTitle();
		this.content = board.getContent();
		this.nickname = board.getMember().getNickname();
		this.prevId = board.getPrev_board() != null ? board.getPrev_board().getId() : null;
		this.nextId = board.getNext_board() != null ? board.getNext_board().getId() : null;
	}
}
