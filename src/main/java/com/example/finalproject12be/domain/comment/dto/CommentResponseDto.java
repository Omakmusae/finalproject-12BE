package com.example.finalproject12be.domain.comment.dto;

import com.example.finalproject12be.domain.comment.entity.Comment;
import com.example.finalproject12be.domain.member.entity.Member;
import com.example.finalproject12be.domain.store.entity.Store;
import com.example.finalproject12be.domain.profile.entity.Profile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

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
    private String imageUrl;

    public CommentResponseDto(Comment comment, Optional<Profile> profile) {
        this.commentId = comment.getId();
        this.memberId = comment.getMember().getId();
        this.storeId = comment.getStore().getId();
        this.nickname = comment.getNickname();
        this.contents = comment.getContents();
        this.check = false; // 기본값으로 false 설정
        this.createdAt = comment.getCreatedAt();
        if (profile.isPresent()) {
            this.imageUrl = profile.get().getImg();
        } else {
            this.imageUrl = ""; // 이미지 URL이 없을 경우 빈 문자열로 설정
        }
    }

    public CommentResponseDto(Comment comment, boolean isCurrentUserComment, Member member, Optional<Profile> profile) {
        this.commentId = comment.getId();
        this.memberId = member != null ? member.getId() : null;
        this.storeId = comment.getStore().getId();
        this.nickname = member != null ? comment.getNickname() : "탈퇴한 회원";
        this.contents = comment.getContents();
        this.check = isCurrentUserComment;
        this.createdAt = comment.getCreatedAt();
        if (profile.isPresent()) {
            this.imageUrl = profile.get().getImg();
        } else {
            this.imageUrl = ""; // 이미지 URL이 없을 경우 빈 문자열로 설정
        }
    }

    public CommentResponseDto(Comment comment, boolean isCurrentUserComment, Store store, Optional<Profile> profile) {
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
        this.foreign = comment.foreign(); // 수정된 부분: Comment의 isForeign() 메서드를 사용하여 isForeign 값을 설정

        if (profile.isPresent()) {
            this.imageUrl = profile.get().getImg();
        } else {
            this.imageUrl = ""; // 이미지 URL이 없을 경우 빈 문자열로 설정
        }
    }



    public void setForeign(boolean foreign) {
        this.foreign = foreign;
    }

    public boolean foreign() {
        return this.foreign;
    }
}