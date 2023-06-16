package com.example.finalproject12be.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode{
	BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
	BOARD_ADMIN_ERROR(HttpStatus.NOT_FOUND, "관리자 권한이 없습니다."),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
