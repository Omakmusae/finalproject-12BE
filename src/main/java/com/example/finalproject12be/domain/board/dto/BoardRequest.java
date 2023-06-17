package com.example.finalproject12be.domain.board.dto;

import com.example.finalproject12be.domain.board.entity.Board;
import com.example.finalproject12be.domain.member.entity.Member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class BoardRequest {

	private String title;
	private String content;

}