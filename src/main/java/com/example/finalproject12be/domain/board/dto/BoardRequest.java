package com.example.finalproject12be.domain.board.dto;

import com.example.finalproject12be.domain.board.entity.Board;
import com.example.finalproject12be.domain.member.entity.Member;

import lombok.Getter;
import lombok.Setter;

@Getter
public class BoardRequest {

	private String title;
	private String content;
	@Setter
	private Member member;

	public static Board toEntity(BoardRequest boardRequest) {

		return Board.of(
			boardRequest.getTitle(),
			boardRequest.getContent(),
			boardRequest.getMember()
		);
	}
}
