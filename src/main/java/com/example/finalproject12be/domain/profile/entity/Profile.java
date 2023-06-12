package com.example.finalproject12be.domain.profile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Profile {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "VALIDNUMBER_ID")
	private Long id;

	@Column
	private String img;

	@Column
	private Long memberId;

	public Profile(String img, Long memberId) {
		this.img = img;
		this.memberId = memberId;
	}
}
