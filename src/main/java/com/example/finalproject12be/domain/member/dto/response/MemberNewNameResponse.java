package com.example.finalproject12be.domain.member.dto.response;

import lombok.Getter;

@Getter
public class MemberNewNameResponse {
	private String nickname;

	public MemberNewNameResponse(String nickname){
		this.nickname = nickname;
	}
}
