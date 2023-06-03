package com.example.finalproject12be.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.finalproject12be.domain.member.entity.Member;
import com.example.finalproject12be.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final MemberRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		// OauthMember kakaoUser = oauthMemberRepository.findByEmail(email)
		// 	.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
		//
		// return new UserDetailsImpl(kakaoUser, kakaoUser.getEmail());

		Member member = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

		 return new UserDetailsImpl(member, member.getEmail());
	}

}

