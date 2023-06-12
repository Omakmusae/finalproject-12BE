package com.example.finalproject12be.domain.validNumber.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.finalproject12be.domain.member.entity.Member;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class ValidNumber {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "VALIDNUMBER_ID")
	private Long id;

	@Column
	private int validNumber;

	@Column
	private String email;

	@Column
	private long time;

	public ValidNumber(int validNumber, String email, long time) {
		this.validNumber = validNumber;
		this.email = email;
		this.time = time;
	}
}
