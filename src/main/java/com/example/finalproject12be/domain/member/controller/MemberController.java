package com.example.finalproject12be.domain.member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.finalproject12be.domain.member.dto.request.MemberLoginRequest;
import com.example.finalproject12be.domain.member.dto.request.MemberSignupRequest;
import com.example.finalproject12be.domain.member.entity.Member;
import com.example.finalproject12be.domain.member.service.MemberService;
import com.example.finalproject12be.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/user/signup")
	public ResponseEntity<Void> signup(@RequestBody final MemberSignupRequest memberSignupRequest) {

		memberService.signup(memberSignupRequest);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@PostMapping("/user/login")
	public ResponseEntity<Void> login(
		@RequestBody final MemberLoginRequest memberLoginRequest,
		final HttpServletResponse response) {

		memberService.login(memberLoginRequest, response);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@GetMapping("user/test")
	public ResponseEntity<Void> test(
		@AuthenticationPrincipal final UserDetailsImpl userDetails
	) {
		Member member = userDetails.getMember();
		System.out.println(userDetails.getUsername());
		System.out.println("테스트 통과");
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@PostMapping("/user/logout")
	public ResponseEntity<String> logout(HttpServletRequest request, final HttpServletResponse response) {
		memberService.logout(request, response);
		return ResponseEntity.ok("로그아웃되었습니다.");
	}
}
