package com.example.finalproject12be.domain.board.dto;

import com.example.finalproject12be.domain.board.entity.Board;
import com.example.finalproject12be.domain.member.entity.MemberRoleEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardResponse {
	private Long id;
	private String title;
	private String content;
	private String nickname;
	private String createdAt;
	private Boolean adminCheck;

	// public BoardResponse(Long id, String title, String content, String nickname) {
	// 	this.id = id;
	// 	this.title = title;
	// 	this.content = content;
	// 	this.nickname = nickname;
	// }

	public BoardResponse(Board board) {
		this.id = board.getId();
		this.title = board.getTitle();
		this.content = board.getContent();
		this.nickname = board.getMember().getNickname();
		this.createdAt = board.getCreatedAt();
		this.adminCheck = board.getMember().getRole() == MemberRoleEnum.ADMIN ? true : false;
	}
}
