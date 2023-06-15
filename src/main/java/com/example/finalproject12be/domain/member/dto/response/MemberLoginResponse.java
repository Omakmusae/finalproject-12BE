package com.example.finalproject12be.domain.member.dto.response;

import lombok.Getter;

@Getter
public class MemberLoginResponse {

	private String email;
	private String nickname;
	private String imageURL;

	//private String Authorization;

	// public MemberLoginResponse(String email, String nickname, String Authorization) {
	// 	this.email = email;
	// 	this.nickname = nickname;
	// 	this.Authorization = Authorization;
	// }
	public MemberLoginResponse(String email, String nickname, String imageURL) {
		this.email = email;
		this.nickname = nickname;
		this.imageURL = imageURL;
	}
	public MemberLoginResponse(String email, String nickname) {
		this.email = email;
		this.nickname = nickname;
		//this.imageURL = null;
	}
}
