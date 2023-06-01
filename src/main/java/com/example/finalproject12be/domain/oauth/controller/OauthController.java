package com.example.finalproject12be.domain.oauth.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<Void> kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) throws JsonProcessingException {
		log.info("컨트롤러야 들어오니?");
		// code: 카카오 서버로부터 받은 인가 코드
		String createToken = oauthMemberService.kakaoLogin(code, response);
		System.out.println(code+"코드닷  !!!!!");
		// Cookie 생성 및 직접 브라우저에 Set
		Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, createToken.substring(7));// 앞부분이 key, 뒷부분이 value
		cookie.setPath("/");
		response.addCookie(cookie);

		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}