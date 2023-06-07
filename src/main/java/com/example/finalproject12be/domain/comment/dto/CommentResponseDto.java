package com.example.finalproject12be.domain.comment.dto;

import com.example.finalproject12be.domain.comment.entity.Comment;
import com.example.finalproject12be.domain.member.entity.Member;
import com.example.finalproject12be.domain.store.entity.Store;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private Long memberId;
    private Long storeId;
    private String nickname;
    private String contents;
    private boolean check;
    private boolean foreign = false;
    private String createdAt;
    private String address;

    private String name;

    private String callNumber;

    private String weekdaysTime;
    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.memberId = comment.getMember().getId();
        this.storeId = comment.getStore().getId();
        this.nickname = comment.getNickname();
        this.contents = comment.getContents();
        this.check = false; // 기본값으로 false 설정
        this.createdAt = comment.getCreatedAt();
    }

    public CommentResponseDto(Comment comment, boolean isCurrentUserComment) {
        this.commentId = comment.getId();
        this.memberId = comment.getMember().getId();
        this.storeId = comment.getStore().getId();
        this.nickname = comment.getNickname();
        this.contents = comment.getContents();
        this.check = isCurrentUserComment;
        this.createdAt = comment.getCreatedAt();
    }

    public CommentResponseDto(Comment comment, boolean isCurrentUserComment, Store store) {
        this.commentId = comment.getId();
        this.memberId = comment.getMember().getId();
        this.storeId = comment.getStore().getId();
        this.nickname = comment.getNickname();
        this.contents = comment.getContents();
        this.check = isCurrentUserComment;
        this.createdAt = comment.getCreatedAt();
        this.address = store.getAddress();
        this.name = store.getName();
        this.callNumber = store.getCallNumber();
        this.weekdaysTime = store.getWeekdaysTime();
        this.foreign = store.getForeignLanguage() != null && store.getForeignLanguage() == 1; // 외국어 가능약국 여부 설정
    }


}