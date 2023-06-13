package com.example.finalproject12be.domain.oauth.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.finalproject12be.domain.member.dto.response.MemberLoginResponse;
import com.example.finalproject12be.domain.oauth.service.OauthMemberService;
import com.example.finalproject12be.security.jwt.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OauthController {

	private final OauthMemberService oauthMemberService;

	@GetMapping("/user/signin/kakao")
	public ResponseEntity<MemberLoginResponse> kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) throws JsonProcessingException {

		String[] tokenArray = oauthMemberService.kakaoLogin(code, response);

		// code: 카카오 서버로부터 받은 인가 코드
		String createAccessToken = tokenArray[0];
		String createRefreshToken = tokenArray[1];
		//String kakaoAccessToken = tokenArray[4]; //로그아웃, 회원탈퇴를 위한 카카오 엑세스 토큰

		// 헤더로 바꿔야함!
		response.addHeader("ACCESS_KEY", createAccessToken);
		response.addHeader("REFRESH_KEY", createRefreshToken);

		MemberLoginResponse loginResult = new MemberLoginResponse(tokenArray[2],tokenArray[3]);
		return ResponseEntity.status(HttpStatus.OK).body(loginResult);
	}
}