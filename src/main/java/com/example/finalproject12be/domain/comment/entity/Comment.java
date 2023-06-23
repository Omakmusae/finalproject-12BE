package com.example.finalproject12be.domain.comment.entity;




import com.example.finalproject12be.domain.store.entity.Store;
import com.example.finalproject12be.domain.comment.dto.CommentRequestDto;
import com.example.finalproject12be.common.Timestamped;

import com.example.finalproject12be.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "STORE_ID")
    @JsonIgnore
    private Store store;
    //Comment -> Store 직렬화를 무시

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String contents;

    @Column(name = "isForeign", nullable = false)
    private boolean foreign;

    public Comment(CommentRequestDto commentRequestDto, Store store, Member member) {
        this.contents = commentRequestDto.getContents();
        this.member = member;
        this.store = store;
        this.nickname = member.getNickname();
        this.foreign = commentRequestDto.isForeign();
    }
    public void deleteMember() {
        this.member = null;
        this.nickname = "(알수없음)"; // 탈퇴한 회원의 경우 닉네임을 "탈퇴한 회원"으로 설정
    }



    public void setIsForeign(boolean foreign) {
        this.foreign = foreign;
    }

    public boolean foreign() {
        return foreign;
    }
}
