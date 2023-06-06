package com.example.finalproject12be.domain.member.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.finalproject12be.domain.member.dto.request.MemberEmailRequest;
import com.example.finalproject12be.domain.member.dto.request.MemberLoginRequest;
import com.example.finalproject12be.domain.member.dto.request.MemberSignupRequest;
import com.example.finalproject12be.domain.member.dto.response.MemberLoginResponse;
import com.example.finalproject12be.domain.member.dto.response.MemberNewNameResponse;
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
	public ResponseEntity<MemberLoginResponse> login(
		@RequestBody final MemberLoginRequest memberLoginRequest,
		final HttpServletResponse response) {

		MemberLoginResponse loginResult = memberService.login(memberLoginRequest, response);
		return ResponseEntity.status(HttpStatus.OK).body(loginResult);
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

	@PostMapping("/user/change/nickname")
	public ResponseEntity<MemberNewNameResponse> changeNickname(
		@RequestBody Map<String, String> newName,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	){
		MemberNewNameResponse changeResult = memberService.changeNickname(newName, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.OK).body(changeResult);
	}


	@DeleteMapping("/user/signout/{email}")
	public ResponseEntity<String> signout(@PathVariable String email) {
		memberService.signout(email);
		return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
	}

	//ing
	@PostMapping("/user/find/password")
	public ResponseEntity<Void> findPassword(
		@RequestBody MemberEmailRequest memberEmailRequest
	){
		memberService.findPassword(memberEmailRequest.getEmail());
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}
