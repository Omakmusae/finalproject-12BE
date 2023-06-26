package com.example.finalproject12be.domain.member.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.springframework.security.crypto.password.PasswordEncoder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.finalproject12be.domain.comment.entity.Comment;
import com.example.finalproject12be.domain.comment.repository.CommentRepository;
import com.example.finalproject12be.domain.member.dto.request.MemberNameRequest;
import com.example.finalproject12be.domain.member.dto.response.MemberLoginResponse;
import com.example.finalproject12be.domain.member.dto.response.MemberNewNameResponse;
import com.example.finalproject12be.domain.member.dto.response.ProfileResponse;
import com.example.finalproject12be.domain.member.entity.MemberRoleEnum;
import com.example.finalproject12be.domain.profile.entity.Profile;
import com.example.finalproject12be.domain.profile.repository.ProfileRepository;
import com.example.finalproject12be.domain.validNumber.entity.ValidNumber;
import com.example.finalproject12be.domain.validNumber.repository.ValidNumberRepository;
import com.example.finalproject12be.exception.CommonErrorCode;
import com.example.finalproject12be.exception.MemberErrorCode;
import com.example.finalproject12be.exception.RestApiException;
import com.example.finalproject12be.exception.ValidNumberErrorCode;
import com.example.finalproject12be.security.UserDetailsImpl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.example.finalproject12be.domain.member.dto.request.MemberLoginRequest;
import com.example.finalproject12be.domain.member.dto.request.MemberSignupRequest;
import com.example.finalproject12be.domain.member.dto.TokenDto;
import com.example.finalproject12be.domain.member.entity.Member;
import com.example.finalproject12be.domain.member.entity.RefreshToken;
import com.example.finalproject12be.domain.member.repository.MemberRepository;
import com.example.finalproject12be.domain.member.repository.RefreshTokenRepository;
import com.example.finalproject12be.security.jwt.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.example.finalproject12be.security.jwt.JwtUtil;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenRepository refreshTokenRepository;
	private final EmailService emailService;
	private final CommentRepository commentRepository;
	private final ValidNumberRepository validNumberRepository;
	private static final String ADMIN_TOKEN = "Odiyac";
	private final AmazonS3Client amazonS3Client;
	private final String S3Bucket = "odimedi-profile";
	private final ProfileRepository profileRepository;

	@Transactional
	public void signup(final MemberSignupRequest memberSignupRequest) {

		throwIfExistOwner(memberSignupRequest.getEmail(), memberSignupRequest.getNickname());
		String password = passwordEncoder.encode(memberSignupRequest.getPassword());

		MemberRoleEnum role = MemberRoleEnum.USER;
		if (memberSignupRequest.isAdmin()) {
			if (!memberSignupRequest.getAdminToken().equals(ADMIN_TOKEN)) {
				throw new RestApiException(MemberErrorCode.ADMIN_ERROR);
			}
			role = MemberRoleEnum.ADMIN;
		}

		Member member = MemberSignupRequest.toEntity(memberSignupRequest, password, role);
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

		TokenDto tokenDto = jwtUtil.createAllToken(searchedMember.getEmail(), searchedMember.getRole());
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

		Optional<Profile> optionalProfile = profileRepository.findByMemberId(searchedMember.getId());

		MemberLoginResponse loginResult;

		if (optionalProfile.isEmpty()) {

			if (searchedMember.getRole() == MemberRoleEnum.ADMIN) {
				loginResult = new MemberLoginResponse(searchedMember.getEmail(), searchedMember.getNickname(), "ADMIN");
			} else  {
				loginResult = new MemberLoginResponse(searchedMember.getEmail(), searchedMember.getNickname(), "USER");
			}

		} else {

			if (searchedMember.getRole() == MemberRoleEnum.ADMIN) {
				loginResult = new MemberLoginResponse(searchedMember.getEmail(), searchedMember.getNickname(),  optionalProfile.get().getImg(), "ADMIN");
			} else  {
				loginResult = new MemberLoginResponse(searchedMember.getEmail(), searchedMember.getNickname(),  optionalProfile.get().getImg(), "USER");
			}
		}

		return loginResult;

	}

	@Transactional
	public void logout(HttpServletRequest request, HttpServletResponse response, Member member) {

		String accessToken = jwtUtil.resolveToken(request, JwtUtil.ACCESS_KEY);
		Optional<RefreshToken> memberToken = refreshTokenRepository.findByEmail(member.getEmail());

		String kakaoAccessToken = "Bearer " + memberToken.get().getKakaoAccessToken();
		String kakaoRefreshToken = "Bearer " + memberToken.get().getKakaoRefreshToken();
		// String[] renewalTokenArray = renewalToken(kakaoAccessToken, memberToken.get().getKakaoRefreshToken());
		// kakaoAccessToken = renewalTokenArray[0];
		// kakaoRefreshToken = renewalTokenArray[1];

		System.out.println(kakaoAccessToken + " $$$$!!!!!!!!!!!");
		if (kakaoAccessToken == null){
			if (accessToken != null) {
				boolean isAccessTokenExpired = jwtUtil.validateToken(accessToken);
				if (!isAccessTokenExpired) {
					Claims accessInfo = jwtUtil.getUserInfoFromToken(accessToken);
					// 액세스 토큰을 무효화하는 작업 수행
					Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
					if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
						UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
						if (accessInfo.getSubject().equals(userDetails.getUsername())) {
							SecurityContextHolder.clearContext();
						}
					}
				}
			}

			String refreshToken = jwtUtil.resolveToken(request, JwtUtil.REFRESH_KEY);

			if (refreshToken != null) {
				boolean isRefreshTokenValid = jwtUtil.refreshTokenValidation(refreshToken);
				if (isRefreshTokenValid) {
					Claims refreshInfo = jwtUtil.getUserInfoFromToken(refreshToken);
					// 리프레시 토큰을 무효화하는 작업 수행
					// 여기에 리프레시 토큰을 저장하는 로직 또는 DB에서 삭제하는 로직을 추가해야 합니다.
				}
			}

			// 로그아웃 후 필요한 작업 수행
			response.setHeader(jwtUtil.ACCESS_KEY, null);
			response.setHeader(jwtUtil.REFRESH_KEY, null);
		}
		else { //소셜 회원 로그아웃 로직
			String reqURL = "https://kapi.kakao.com/v1/user/logout";
			try {
				URL url = new URL(reqURL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Authorization", kakaoAccessToken);

				int responseCode = conn.getResponseCode();
				System.out.println("responseCode : " + responseCode);

				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

				String result = "";
				String line = "";

				while ((line = br.readLine()) != null) {
					result += line;
				}
				System.out.println(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// HttpHeaders headers = new HttpHeaders();
			// headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			// headers.add("Authorization", kakaoAccessToken);
			//
			// // HTTP Body 생성
			// MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
			// body.add("message", "로그아웃");
			//
			// HttpEntity<MultiValueMap<String, String>> kakaoTokenForLogout =
			// 	new HttpEntity<>(body, headers);
			// RestTemplate rt = new RestTemplate();
			//
			// ResponseEntity<String> responseLogout = rt.exchange(
			// 	URI.create("https://kapi.kakao.com/v1/user/logout"),
			// 	HttpMethod.POST,
			// 	kakaoTokenForLogout,
			// 	String.class
			// );
			// System.out.println("로그아웃 완료!!!!!!!!!!!!!!!!!!!!");
		}

	}

	@Transactional
	public void signout(Member requestMember, final HttpServletRequest request) {

		Member member = memberRepository.findByNickname(requestMember.getNickname())
				.orElseThrow(() ->  new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

		Optional<RefreshToken> memberToken = refreshTokenRepository.findByEmail(member.getEmail());
		String kakaoAccessToken = "Bearer " + memberToken.get().getKakaoAccessToken();
		String kakaoRefreshToken = "Bearer " + memberToken.get().getKakaoRefreshToken();

		// String[] renewalTokenArray = renewalToken(kakaoAccessToken, kakaoRefreshToken);
		// kakaoAccessToken = "Bearer " + renewalTokenArray[0];
		// kakaoRefreshToken = "Bearer " + renewalTokenArray[1];

		if (member.getKakaoId() == null) {
			// 카카오 소셜 로그인이 아닌 일반 가입 회원의 경우 직접 삭제
			List<Comment> comments = commentRepository.findByMember(member);
			for (Comment comment : comments) {
				// memberId를 임의로 변경합니다.
				comment.deleteMember();
				comment.setNickname("(알수없음)");
			}
			memberRepository.delete(member);
			refreshTokenRepository.deleteRefreshTokenByEmail(member.getEmail());

		} else {
			// 카카오 소셜 로그인 회원의 경우 카카오 계정 연결 해제 후 삭제
			List<Comment> comments = commentRepository.findByMember(member);
			for (Comment comment : comments) {
				// memberId를 임의로 변경합니다.
				comment.deleteMember();
				comment.setNickname("(알수없음)");
			}
			disconnectKakaoAccount(kakaoAccessToken);
			refreshTokenRepository.deleteRefreshTokenByEmail(member.getEmail());
			commentRepository.deleteCommentsByMemberId(member.getId());//수정 필요
			memberRepository.delete(member);

		}
	}

	private void disconnectKakaoAccount(String kakaoAccessToken) {
		// *** 카카오 API를 사용하여 카카오 계정 연결 해제 로직 구현해주셔야합니다 ***
		// *** 카카오 계정 연결 해제 작업 수행 ***
		// HTTP Header 생성
		String reqURL = "https://kapi.kakao.com/v1/user/unlink";
		System.out.println(kakaoAccessToken + "탈퇴 시작 로직 시작 !!!!!!!!! ");
		try {
			// HTTP Header 생성
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			headers.add("Authorization", kakaoAccessToken);

			// HTTP Body 생성
			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add("message", "탈퇴 완료");

			// HTTP 요청을 보낼 RestTemplate 객체 생성
			RestTemplate restTemplate = new RestTemplate();

			// HTTP 요청을 보내고 응답 받기
			ResponseEntity<String> response = restTemplate.exchange(
				reqURL,
				HttpMethod.POST,
				new HttpEntity<>(body, headers),
				String.class
			);

			// 응답코드 확인
			int responseCode = response.getStatusCodeValue();
			System.out.println("responseCode : " + responseCode);

			// 응답 결과 출력
			String result = response.getBody();
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(kakaoAccessToken + "탈퇴 시작 로직 마무리 !!!!!!!!! ");
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
	public MemberNewNameResponse changeNickname(String newName, Member member) {

			Optional<Member> memberOptional = memberRepository.findByNickname(newName);

			if(memberOptional.isPresent()){
				throw new RestApiException(MemberErrorCode.DUPLICATED_MEMBER);
			}

			member.updateName(newName);
			memberRepository.save(member);

			return new MemberNewNameResponse(newName);

	}

	//ing
	@Transactional
	public void findPassword(String email) {

		Optional<Member> memberOptional = memberRepository.findByEmail(email);

		//로그인 한 유저의 이메일과 요청받은 이메일이 같은가?
		//TODO: 예외 던지기
		if(memberOptional.isPresent()){
			Member member = memberOptional.get();

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

			emailService.sendPassword(newPassword, email);
		}else{
			throw new RestApiException(MemberErrorCode.INACTIVE_MEMBER);
		}
	}

	@Transactional
	public void changePassword(String password, Member member) {

		String encodePw = passwordEncoder.encode(password);
		member.updatePassword(encodePw);
		memberRepository.save(member);

	}

	@Transactional
	public MemberNewNameResponse changeNicknameAdmin(String newName, Member member, String nickname) {
		MemberRoleEnum memberRoleEnum =  member.getRole();
		if (memberRoleEnum != MemberRoleEnum.ADMIN) {
			throw new RestApiException(MemberErrorCode.INACTIVE_MEMBER);
		}

		Member memberBeforeUpdate = memberRepository.findByNickname(nickname)
			.orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

		Optional<Member> memberOptional = memberRepository.findByNickname(nickname);

		if(!memberOptional.isPresent()){
			throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
		}

		memberBeforeUpdate.updateName(newName);

		return new MemberNewNameResponse(newName);

	}

	@Transactional
	public void signoutAdmin(MemberNameRequest memberNameRequest, Member member, final HttpServletRequest request) {

		MemberRoleEnum memberRoleEnum =  member.getRole();
		if (memberRoleEnum != MemberRoleEnum.ADMIN) {
			throw new RestApiException(MemberErrorCode.INACTIVE_MEMBER);
		}

		Member deleteMember = memberRepository.findByNickname(memberNameRequest.getNewName())
			.orElseThrow(() ->  new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

		List<Comment> comments = commentRepository.findByNickname(memberNameRequest.getNewName());
		for (Comment comment : comments) {
			// memberId를 임의로 변경합니다.
			comment.deleteMember();
			comment.setNickname("(알수없음)");
		}

		memberRepository.delete(deleteMember);
		refreshTokenRepository.deleteById(deleteMember.getId());

	}

	//ing
	public void checkEmail(String email) {

		//해당 이메일로 전에 인증번호를 요청했다면 전 인증번호를 db에서 삭제함
		Optional<ValidNumber> pastNumber = validNumberRepository.findByEmail(email);
		if(pastNumber.isPresent()){
			validNumberRepository.delete(pastNumber.get());
		}

		Optional<Member> memberOptional = memberRepository.findByEmail(email);

		if(memberOptional.isPresent()){
			throw new RestApiException(MemberErrorCode.DUPLICATED_EMAIL);
		}

		// int number = (int)((Math.random()*10000)%10);
		int number = (int)(Math.random() * 899999) + 100000;

		LocalTime now = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
		long formatedNow = Long.parseLong(now.format(formatter));

		ValidNumber validNumber = new ValidNumber(number, email, formatedNow);
		validNumberRepository.save(validNumber);

		emailService.sendNumber(number, email);
	}

	public boolean checkValidNumber(int number, String email) {
		boolean checkNumber = true;

		LocalTime now = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
		long formatedNow = Long.parseLong(now.format(formatter));

		Optional<ValidNumber> validNumberOptional = validNumberRepository.findByEmail(email);

		if(!validNumberOptional.isPresent()){
			throw new RestApiException(CommonErrorCode.INVALID_REQUEST_PARAMETER); //이메일로 인증번호를 찾을 수 없음
		}

		ValidNumber validNumber = validNumberOptional.get();
		double time = validNumber.getTime();

		if(formatedNow - time >= 300){ // 인증번호 발급 받은지 3분 초과
			// validNumberRepository.delete(validNumber);
			throw new RestApiException(ValidNumberErrorCode.VALID_TIME_OVER);
		}

		if(number != validNumber.getValidNumber()){
			throw new RestApiException(ValidNumberErrorCode.WRONG_NUMBER);
		}else{
			return checkNumber;
		}
	}

	public ProfileResponse uploadProfile(MultipartFile file, Member member) {

		if(file.isEmpty()){
			throw new RestApiException(CommonErrorCode.INVALID_REQUEST_PARAMETER);
		}

		Long memberId = member.getId();
		Optional<Profile> optionalProfile = profileRepository.findByMemberId(memberId);

		if(optionalProfile.isPresent()){
			Profile pastProfile = optionalProfile.get();
			profileRepository.delete(pastProfile);
		}

		String imagePath = saveImg(file);
		Profile profile = new Profile(imagePath, memberId);
		profileRepository.save(profile);

		return new ProfileResponse(imagePath);
	}

	private String saveImg (final MultipartFile file) {

		String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
		long size = file.getSize();
		ObjectMetadata objectMetaData = new ObjectMetadata();
		objectMetaData.setContentType(file.getContentType());
		objectMetaData.setContentLength(size);

		try {
			amazonS3Client.putObject(
				new PutObjectRequest(S3Bucket, fileName, file.getInputStream(), objectMetaData)
					.withCannedAcl(CannedAccessControlList.PublicRead)
			);
		} catch (IOException e) {
			throw new RestApiException(CommonErrorCode.IO_EXCEPTION);
		}

		return amazonS3Client.getUrl(S3Bucket, fileName).toString();
	}

	private String[] renewalToken (String kakaoAccessToken, String kakaoRefreshToken) {

		String reqURL = "https://kauth.kakao.com/oauth/token";
		System.out.println(kakaoRefreshToken + "갱신 시작 !!!!!!!!! ");
		String[] tokenArray = new String[2];
		try {
			// HTTP Header 생성
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

			// HTTP Body 생성
			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add("grant_type", "refresh_token");
			body.add("client_id", "048f9445160611c1cc986c481c2d6b94");//내 앱 rest api 키
			//body.add("client_id", "111b5867f4dff0156fb3f17736d40f3e");//유리님 앱 rest api 키

			body.add("refresh_token", kakaoRefreshToken);

			// HTTP 요청을 보낼 RestTemplate 객체 생성
			RestTemplate restTemplate = new RestTemplate();

			// HTTP 요청을 보내고 응답 받기
			ResponseEntity<String> response = restTemplate.exchange(
				reqURL,
				HttpMethod.POST,
				new HttpEntity<>(body, headers),
				String.class
			);
			System.out.println( "HTTP 보내기 시작 !!!!!!!!! ");
			// HTTP 응답 (JSON) -> 액세스 토큰 파싱
			String responseBody = response.getBody();
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(responseBody);
			System.out.println( "HTTP 받기 시작 !!!!!!!!! ");

			tokenArray[0] = jsonNode.get("access_token").asText();
			tokenArray[1] = jsonNode.get("refresh_token").asText();
			System.out.println(tokenArray[0] + "갱신 마무리 !!!!!!!!! ");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return tokenArray;
	}
}
