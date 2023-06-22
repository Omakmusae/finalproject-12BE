package com.example.finalproject12be.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ValidNumberErrorCode implements ErrorCode {

	VALID_TIME_OVER(HttpStatus.BAD_REQUEST, "만료시간이 지난 인증번호입니다."),
	WRONG_NUMBER(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다.")
	;

	private final HttpStatus httpStatus;
	private final String message;
}