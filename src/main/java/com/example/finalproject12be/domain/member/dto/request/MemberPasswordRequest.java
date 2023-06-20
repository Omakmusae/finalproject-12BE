package com.example.finalproject12be.domain.member.dto.request;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class MemberPasswordRequest {

	// @Size(min = 8, max = 15)
	@Pattern(regexp = "^[0-9a-zA-Z]{8,15}$", message = "비밀번호는 8~15자 알파벳 대소문자, 숫자로 작성해주세요.")
	private String newPassword;

}
