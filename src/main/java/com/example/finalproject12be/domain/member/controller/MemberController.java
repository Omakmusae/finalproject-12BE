package com.example.finalproject12be.domain.member.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.finalproject12be.domain.bookmark.entity.Bookmark;
import com.example.finalproject12be.domain.member.dto.request.MemberEmailRequest;
import com.example.finalproject12be.domain.member.dto.request.MemberLoginRequest;
import com.example.finalproject12be.domain.member.dto.request.MemberNameRequest;
import com.example.finalproject12be.domain.member.dto.request.MemberNameRequestAdmin;
import com.example.finalproject12be.domain.member.dto.request.MemberPasswordRequest;
import com.example.finalproject12be.domain.member.dto.request.MemberSignupRequest;
import com.example.finalproject12be.domain.member.dto.request.MemberValidNumberRequest;
import com.example.finalproject12be.domain.member.dto.response.MemberLoginResponse;
import com.example.finalproject12be.domain.member.dto.response.MemberNewNameResponse;
import com.example.finalproject12be.domain.member.dto.response.ProfileResponse;
import com.example.finalproject12be.domain.member.entity.Member;
import com.example.finalproject12be.domain.member.service.MemberService;
import com.example.finalproject12be.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/api/user/signup")
	public ResponseEntity<String> signup(@RequestBody @Valid final MemberSignupRequest memberSignupRequest) {
		memberService.signup(memberSignupRequest);
		return ResponseEntity.status(HttpStatus.OK).body("회원가입 완료");
	}

	@PostMapping("/api/user/login")
	public ResponseEntity<MemberLoginResponse> login(
		@RequestBody final MemberLoginRequest memberLoginRequest,
		final HttpServletResponse response) {
		MemberLoginResponse loginResult = memberService.login(memberLoginRequest, response);
		return ResponseEntity.status(HttpStatus.OK).body(loginResult);
	}

	@PostMapping("/api/user/logout")
	public ResponseEntity<String> logout(HttpServletRequest request,
		final HttpServletResponse response,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		memberService.logout(request, response, userDetails.getMember());
		return ResponseEntity.ok("로그아웃되었습니다.");
	}

	@PostMapping("/api/user/change/nickname")
	public ResponseEntity<MemberNewNameResponse> changeNickname(
		@RequestBody @Valid MemberNameRequest memberNameRequest,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		MemberNewNameResponse changeResult = memberService.changeNickname(memberNameRequest.getNewName(),
			userDetails.getMember());
		return ResponseEntity.status(HttpStatus.OK).body(changeResult);
	}

	@DeleteMapping("/api/user/signout")
	public ResponseEntity<String> signout(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		final HttpServletRequest request) {
		memberService.signout(userDetails.getMember(), request);
		return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
	}

	@PostMapping("/api/user/find/password")
	public ResponseEntity<Void> findPassword(
		@RequestBody @Valid MemberEmailRequest memberEmailRequest
	) {
		memberService.findPassword(memberEmailRequest.getEmail());
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@PostMapping("/api/user/change/password")
	public ResponseEntity<Void> changePassword(
		@RequestBody @Valid MemberPasswordRequest memberPasswordRequest,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		memberService.changePassword(memberPasswordRequest.getNewPassword(), userDetails.getMember());
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@PutMapping("/api/user/change/nickname/admin")
	public ResponseEntity<MemberNewNameResponse> changeNicknameAdmin(
		@RequestBody @Valid MemberNameRequestAdmin memberNameRequest,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		MemberNewNameResponse changeResult = memberService.changeNicknameAdmin(memberNameRequest.getNewName(),
			userDetails.getMember(), memberNameRequest.getNickname());
		return ResponseEntity.status(HttpStatus.OK).body(changeResult);
	}

	@DeleteMapping("/api/user/signout/admin")
	public ResponseEntity<String> signOutAdmin(
		@RequestBody @Valid MemberNameRequest memberNameRequest, @AuthenticationPrincipal UserDetailsImpl userDetails,
		final HttpServletRequest request) {
		memberService.signoutAdmin(memberNameRequest, userDetails.getMember(), request);
		return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
	}

	@PostMapping("/api/user/change/profile")
	public ResponseEntity<ProfileResponse> uploadProfile(
		@RequestPart(value = "file") final MultipartFile file,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	){
		ProfileResponse profileResponse = memberService.uploadProfile(file, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.OK).body(profileResponse);
	}

	@PostMapping("/api/user/signup/email")
	public ResponseEntity<Void> checkEmail(
		@RequestBody @Valid MemberEmailRequest memberEmailRequest){
		memberService.checkEmail(memberEmailRequest.getEmail());
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@PostMapping("/api/user/signup/email/valid")
	public ResponseEntity<Boolean> checkValidNumber(
		@RequestBody MemberValidNumberRequest memberValidNumberRequest
	){
		boolean checkNumber = memberService.checkValidNumber(memberValidNumberRequest.getValidNumber(), memberValidNumberRequest.getEmail());
		return ResponseEntity.status(HttpStatus.OK).body(checkNumber);
	}
}