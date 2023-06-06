package com.example.finalproject12be.domain.member.dto.response;

import lombok.Getter;

@Getter
public class MemberLoginResponse {
	private String email;
	private String password;

	public MemberLoginResponse(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
