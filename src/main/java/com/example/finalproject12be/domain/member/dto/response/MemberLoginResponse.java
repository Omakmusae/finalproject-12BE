package com.example.finalproject12be.domain.member.dto.response;

import lombok.Getter;

@Getter
public class MemberLoginResponse {
	private String email;
	private String nickname;

	public MemberLoginResponse(String email, String nickname) {
		this.email = email;
		this.nickname = nickname;
	}
}
