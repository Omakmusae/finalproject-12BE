package com.example.finalproject12be.domain.board.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.example.finalproject12be.common.Timestamped;
import com.example.finalproject12be.domain.board.dto.BoardRequest;
import com.example.finalproject12be.domain.member.entity.Member;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Data
@Entity
public class Board extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BOARD_ID")
	private Long id;

	@Column(nullable = false)
	private String title;

	@Lob
	@Column(nullable = false)
	private String content;

	@Transient
	private Board prev_board;

	@Transient
	private Board next_board;

	@ManyToOne
	@JoinColumn(name="MEMBER_ID")
	private Member member;

	private Board(String title, String content, Member member) {
		this.title = title;
		this.content = content;
		this.member = member;
	}

	public void updateBoard(BoardRequest boardRequest){
		this.title = boardRequest.getTitle();
		this.content = boardRequest.getContent();
	}
	public static Board of(String title, String content, Member member) {
		return new Board(title, content, member);
	}
}
