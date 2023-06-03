package com.example.finalproject12be.domain.bookmark.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.finalproject12be.domain.member.entity.Member;
import com.example.finalproject12be.domain.store.entity.Store;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Bookmark {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BOOKMARK_ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "MEMBER_ID")
	@JsonBackReference
	private Member member;

	@ManyToOne
	@JoinColumn(name = "STORE_ID")
	@JsonBackReference
	private Store store;

	public Bookmark(Store store, Member member){
		this.store = store;
		this.member = member;
	}

}
