package com.example.finalproject12be.domain.comment.dto;

import com.example.finalproject12be.domain.comment.entity.Comment;
import com.example.finalproject12be.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private Long memberId;
    private Long storeId;
    private String nickname;
    private String contents;
    private boolean check;
    private String createdAt;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.memberId = comment.getMember().getId();
        this.storeId = comment.getStore().getId();
        this.nickname = comment.getNickname();
        this.contents = comment.getContents();
        this.check = false; // 기본값으로 false 설정
        this.createdAt = comment.getCreatedAt();
    }

    public CommentResponseDto(Comment comment, boolean check) {
        this.commentId = comment.getId();
        this.memberId = comment.getMember().getId();
        this.storeId = comment.getStore().getId();
        this.nickname = comment.getNickname();
        this.contents = comment.getContents();
        this.check = check;
        this.createdAt = comment.getCreatedAt();
    }
}