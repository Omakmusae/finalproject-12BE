package com.example.finalproject12be.domain.board.dto;

import com.example.finalproject12be.domain.board.entity.Board;
import com.example.finalproject12be.domain.member.entity.Member;

import lombok.Getter;


@Getter
public class MappedBoardRequest {

	private String title;
	private String content;
	private Member member;

	public MappedBoardRequest(String title, String content, Member member) {
		this.title = title;
		this.content = content;
		this.member = member;
	}

	public static Board toEntity(MappedBoardRequest boardRequest) {
		return Board.of (
			boardRequest.getTitle(),
			boardRequest.getContent(),
			boardRequest.getMember()
		);
	}

}

