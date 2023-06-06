package com.example.finalproject12be.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ErrorResponse {

	private final String errorCode;
	private final String status;
	private final String message;

	public ErrorResponse(String errorCode, String status, String message) {
		this.errorCode = errorCode;
		this.status = status;
		this.message = message;
	}
}
