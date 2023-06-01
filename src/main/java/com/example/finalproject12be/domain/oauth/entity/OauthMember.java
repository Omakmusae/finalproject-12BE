package com.example.finalproject12be.domain.oauth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class OauthMember {
	@Id
	@Column(name = "OAUTHMEMBER_ID")
	private Long id;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String nickname;

	public OauthMember(Long id, String email, String nickname) {
		this.id = id;
		this.email = email;
		this.nickname = nickname;
	}


}
