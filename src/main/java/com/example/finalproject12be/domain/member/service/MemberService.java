package com.example.finalproject12be.domain.member.service;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.finalproject12be.domain.member.dto.response.MemberLoginResponse;
import com.example.finalproject12be.domain.member.dto.response.MemberNewNameResponse;
import com.example.finalproject12be.exception.MemberErrorCode;
import com.example.finalproject12be.exception.RestApiException;
import com.example.finalproject12be.security.UserDetailsImpl;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
	private final EmailService emailService;

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
			() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

		System.out.println(password);
		System.out.println(searchedMember.getPassword());

		if(!passwordEncoder.matches(password, searchedMember.getPassword())){
			throw new RestApiException(MemberErrorCode.INVALID_PASSWORD);
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

	@Transactional
	public void signout(String email) {
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() ->  new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

		if (member.getKakaoId() == null) {
			// 카카오 소셜 로그인이 아닌 일반 가입 회원의 경우 직접 삭제
			memberRepository.delete(member);
		} else {
			// 카카오 소셜 로그인 회원의 경우 카카오 계정 연결 해제 후 삭제
			disconnectKakaoAccount(member);
			memberRepository.delete(member);
		}
	}

	private void disconnectKakaoAccount(Member member) {
		// *** 카카오 API를 사용하여 카카오 계정 연결 해제 로직 구현해주셔야합니다 ***
		// *** 카카오 계정 연결 해제 작업 수행 ***
	}

	private void throwIfExistOwner(String loginEmail, String loginNickName) {

		Optional<Member> searchedEmail = memberRepository.findByEmail(loginEmail);
		Optional<Member> searchedNickName = memberRepository.findByNickname(loginNickName);

		if (searchedEmail.isPresent()) {
			throw new RestApiException(MemberErrorCode.DUPLICATED_EMAIL);
		}

		if(searchedNickName.isPresent()){
			throw new RestApiException(MemberErrorCode.DUPLICATED_MEMBER);
		}
	}

	// @Transactional
	public MemberNewNameResponse changeNickname(Map newName, Member member) {
		String nickname = String.valueOf(newName.get("newName"));
		Optional<Member> memberOptional = memberRepository.findByNickname(nickname);

		if(memberOptional.isPresent()){
			throw new RestApiException(MemberErrorCode.DUPLICATED_MEMBER);
		}

		member.updateName(nickname);
		memberRepository.save(member);

		return new MemberNewNameResponse(nickname);
	}

	//ing
	public void findPassword(String email) {

		Optional<Member> memberOptional = memberRepository.findByEmail(email);

		//로그인 한 유저의 이메일과 요청받은 이메일이 같은가?
		//TODO: 예외 던지기
		if(memberOptional.isPresent()){
			Member member = memberOptional.get();
			if(memberOptional.get().getId().equals(member.getId())){

				int leftLimit = 97; // letter 'a'
				int rightLimit = 122; // letter 'z'
				int targetStringLength = 10;
				Random random = new Random();
				String newPassword = random.ints(leftLimit, rightLimit + 1)
					.limit(targetStringLength)
					.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
					.toString();

				String encodePw = passwordEncoder.encode(newPassword);
				member.updatePassword(encodePw);
				memberRepository.save(member);

				emailService.sendMail(newPassword, email);

			}else {
				throw new RestApiException(MemberErrorCode.INACTIVE_MEMBER);
			}
		}else{
			throw new RestApiException(MemberErrorCode.DUPLICATED_EMAIL);
		}
	}

	public void changePassword(String password, Member member) {

		String encodePw = passwordEncoder.encode(password);
		member.updatePassword(encodePw);
		memberRepository.save(member);

	}

	// @Override

	// public void mailSend() {
	//
	// 	@Autowired
	// 	MailSender mailSender;
	//
	//
	//
	// 	// System.out.println("전송 완료!");
	// 	SimpleMailMessage message = new SimpleMailMessage();
	// 	message.setTo("kmskes1125@gmail.com"); //수신자 설정
	// 	message.setSubject("오디약! 비밀번호 변경"); //메일 제목
	// 	message.setText("이건 메일 내용이고 임시 비밀번호를 보낼 예정입니다."); //메일 내용 설정
	// 	message.setFrom("kmskes0917@naver.com"); //발신자 설정
	// 	// message.setReplyTo("보낸이@naver.com");
	// 	// System.out.println("message"+message);
	// 	mailSender.send(message);
	// }
}
