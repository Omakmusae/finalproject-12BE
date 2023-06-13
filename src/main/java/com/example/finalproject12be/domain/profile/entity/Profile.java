package com.example.finalproject12be.domain.profile.entity;

import javax.persistence.*;

import com.example.finalproject12be.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Profile {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PROFILE_ID")
	private Long id;

	@Column
	private String img;

	@Column
	private Long memberId;

	@OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
	private Member member;

	public Profile(String img, Long memberId) {
		this.img = img;
		this.memberId = memberId;
	}
}
