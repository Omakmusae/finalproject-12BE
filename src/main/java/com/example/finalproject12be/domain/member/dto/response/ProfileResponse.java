package com.example.finalproject12be.domain.member.dto.response;

import lombok.Getter;

@Getter
public class ProfileResponse {

	private String file;

	public ProfileResponse(String file) {
		this.file = file;
	}
}
