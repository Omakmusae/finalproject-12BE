package com.example.finalproject12be.domain.member.entity;

import java.util.List;

import javax.persistence.*;

import com.example.finalproject12be.domain.board.entity.Board;
import com.example.finalproject12be.domain.bookmark.entity.Bookmark;

import com.example.finalproject12be.domain.profile.entity.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MEMBER_ID")
	private Long id;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String nickname;

	@Column(nullable = true)
	private Long kakaoId;

	@Column(nullable = true)
	@Enumerated(value = EnumType.STRING)
	private MemberRoleEnum role;

	@OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<Bookmark> bookmarks;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "MEMBER_ID", referencedColumnName = "MEMBER_ID")
	private Profile profile;

	public Member(String email, String password, String nickname, MemberRoleEnum role) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.role = role;
	}

	public Member(String email, String password, String nickname, Long kakaoId, MemberRoleEnum role ) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.kakaoId = kakaoId;
		this.role = role;
	}

	public static Member of(String email, String password, String nickname, MemberRoleEnum role) {
		return new Member(email, password, nickname, role);
	}

	public void deleteBookmark(Bookmark bookmark){
		this.bookmarks.remove(bookmark);
	}

	public void addBookmark(Bookmark bookmark){
		this.bookmarks.add(bookmark);
	}

	public Member kakaoIdUpdate(Long kakaoId) {
		this.kakaoId = kakaoId;
		return this;
	}

	public void updateName(String newName){
		this.nickname = newName;
	}

	public void updatePassword(String newPassword) {
		this.password = newPassword;
	}



}
