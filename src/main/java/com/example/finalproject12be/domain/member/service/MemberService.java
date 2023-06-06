package com.example.finalproject12be.domain.member.service;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.finalproject12be.domain.member.dto.response.MemberLoginResponse;
import com.example.finalproject12be.exception.MemberErrorCode;
import com.example.finalproject12be.exception.RestApiException;
import com.example.finalproject12be.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.finalproject12be.domain.member.dto.request.MemberLoginRequest;
import com.example.finalproject12be.domain.member.dto.request.MemberSignupRequest;
import com.example.finalproject12be.domain.member.dto.TokenDto;
import com.example.finalproject12be.domain.member.entity.Member;
import com.example.finalproject12be.domain.member.entity.RefreshToken;
import com.example.finalproject12be.domain.member.repository.MemberRepository;
import com.example.finalproject12be.domain.member.repository.RefreshTokenRepository;
import com.example.finalproject12be.security.jwt.JwtUtil;
//import com.example.finalproject12be.security.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public void signup(final MemberSignupRequest memberSignupRequest) {

		throwIfExistOwner(memberSignupRequest.getEmail(), memberSignupRequest.getNickname());
		String password = passwordEncoder.encode(memberSignupRequest.getPassword());
		Member member = MemberSignupRequest.toEntity(memberSignupRequest, password);
		memberRepository.save(member);
	}

	@Transactional
	public MemberLoginResponse login(final MemberLoginRequest memberLoginRequest, final HttpServletResponse response) {

		String email = memberLoginRequest.getEmail();
		String password = memberLoginRequest.getPassword();
		Member searchedMember = memberRepository.findByEmail(email).orElseThrow(
			() -> new IllegalArgumentException("등록된 사용자가 없습니다.")
		);

		System.out.println(password);
		System.out.println(searchedMember.getPassword());

		if(!passwordEncoder.matches(password, searchedMember.getPassword())){
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		TokenDto tokenDto = jwtUtil.createAllToken(email);
		Optional<RefreshToken> refreshToken = refreshTokenRepository.findByEmail(email);

		if(refreshToken.isPresent()) {
			RefreshToken updateToken = refreshToken.get().updateToken(tokenDto.getRefreshToken().substring(7));
			refreshTokenRepository.save(updateToken);
		} else {
			RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken().substring(7), memberLoginRequest.getEmail());
			refreshTokenRepository.save(newToken);
		}

		response.addHeader(jwtUtil.ACCESS_KEY, tokenDto.getAccessToken());
		response.addHeader(jwtUtil.REFRESH_KEY, tokenDto.getRefreshToken());

		MemberLoginResponse loginResult = new MemberLoginResponse(searchedMember.getEmail(), searchedMember.getNickname());
		return loginResult;

	}

	public void logout(HttpServletRequest request, HttpServletResponse response) {
		String accessToken = jwtUtil.resolveToken(request, JwtUtil.ACCESS_KEY);
		if (accessToken != null) {
			boolean isAccessTokenExpired = jwtUtil.validateToken(accessToken);
			if (!isAccessTokenExpired) {
				String username = jwtUtil.getUserInfoFromToken(accessToken);
				// 액세스 토큰을 무효화하는 작업 수행
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
					UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
					if (username.equals(userDetails.getUsername())) {
						SecurityContextHolder.clearContext();
					}
				}
			}
		}

		String refreshToken = jwtUtil.resolveToken(request, JwtUtil.REFRESH_KEY);
		if (refreshToken != null) {
			boolean isRefreshTokenValid = jwtUtil.refreshTokenValidation(refreshToken);
			if (isRefreshTokenValid) {
				String username = jwtUtil.getUserInfoFromToken(refreshToken);
				// 리프레시 토큰을 무효화하는 작업 수행
				// 여기에 리프레시 토큰을 저장하는 로직 또는 DB에서 삭제하는 로직을 추가해야 합니다.
			}
		}

		// 로그아웃 후 필요한 작업 수행
		response.setHeader(jwtUtil.ACCESS_KEY, null);
		response.setHeader(jwtUtil.REFRESH_KEY, null);
	}

	private void throwIfExistOwner(String loginEmail, String loginNickName) {

		Optional<Member> searchedEmail = memberRepository.findByEmail(loginEmail);
		Optional<Member> searchedNickName = memberRepository.findByNickname(loginNickName);

		if (searchedEmail.isPresent()) {
			throw new IllegalArgumentException("가입된 이메일입니다.");
		}

		if(searchedNickName.isPresent()){
			throw new IllegalArgumentException("가입된 닉네임입니다.");
		}
	}

	// @Transactional
	public void changeNickname(Map newName, Member member) {
		String nickname = String.valueOf(newName.get("newName"));
		Optional<Member> memberOptional = memberRepository.findByNickname(nickname);

		if(memberOptional.isPresent()){
			throw new RestApiException(MemberErrorCode.DUPLICATED_MEMBER);
		}

		member.updateName(nickname);
		memberRepository.save(member);
	}

	//ing
	// public void findPassword(String email, Member member) {
	//
	// 	Optional<Member> memberOptional = memberRepository.findByEmail(email);
	//
	// 	//로그인 한 유저의 이메일과 요청받은 이메일이 같은가?
	// 	//TODO: 예외 던지기
	// 	if(memberOptional.isPresent()){
	// 		if(memberOptional.get().equals(member)){
	//
	//
	//
	// 		}
	// 	}
	// }
}
