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
	private String createdAt;

	private Long prevId;
	private String prevTitle;
	private String prevNickname;
	private String prevCreatedAt;

	private Long nextId;
	private String nextTitle;
	private String nextNickname;
	private String nextCreatedAt;


	public BoardDetailResponse(Board board) {
		this.id = board.getId();
		this.title = board.getTitle();
		this.content = board.getContent();
		this.nickname = board.getMember().getNickname();
		this.createdAt = board.getCreatedAt();

		this.prevId = board.getPrev_board() != null ? board.getPrev_board().getId() : null;
		this.prevTitle = board.getPrev_board() != null ? board.getPrev_board().getTitle() : null;
		this.prevNickname = board.getPrev_board() != null ? board.getPrev_board().getMember().getNickname() : null;
		this.prevCreatedAt = board.getPrev_board() != null ? board.getPrev_board().getCreatedAt() : null;

		this.nextId = board.getNext_board() != null ? board.getNext_board().getId() : null;
		this.nextTitle = board.getNext_board() != null ? board.getNext_board().getTitle() : null;
		this.nextNickname = board.getNext_board() != null ? board.getNext_board().getMember().getNickname() : null;
		this.nextCreatedAt = board.getNext_board() != null ? board.getNext_board().getCreatedAt() : null;

	}
}
