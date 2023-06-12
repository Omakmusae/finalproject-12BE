package com.example.finalproject12be.domain.board.dto;

import com.example.finalproject12be.domain.board.entity.Board;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardResponse {
	private Long id;
	private String title;
	private String content;
	private String nickname;

	public static BoardResponse from (Board entity) {

		return new BoardResponse(
			entity.getId(),
			entity.getTitle(),
			entity.getContent(),
			entity.getMember().getNickname()
		);
	}
}
