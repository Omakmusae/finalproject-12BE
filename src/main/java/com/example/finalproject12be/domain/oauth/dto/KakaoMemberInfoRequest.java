package com.example.finalproject12be.domain.oauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoMemberInfoRequest {

	private Long id;
	private String email;
	private String nickname;

	public KakaoMemberInfoRequest(String email, String nickname) {

		this.email = email;
		this.nickname = nickname;
	}
}
