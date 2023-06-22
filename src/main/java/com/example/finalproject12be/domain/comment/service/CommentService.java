package com.example.finalproject12be.domain.comment.service;

import com.example.finalproject12be.common.ResponseMsgDto;
import com.example.finalproject12be.domain.comment.dto.CommentRequestDto;
import com.example.finalproject12be.domain.comment.dto.CommentResponseDto;
import com.example.finalproject12be.domain.comment.entity.Comment;
import com.example.finalproject12be.domain.comment.repository.CommentRepository;
import com.example.finalproject12be.domain.member.entity.Member;
import com.example.finalproject12be.domain.member.repository.MemberRepository;
import com.example.finalproject12be.domain.profile.entity.Profile;
import com.example.finalproject12be.domain.profile.repository.ProfileRepository;
import com.example.finalproject12be.domain.store.entity.Store;
import com.example.finalproject12be.domain.store.repository.StoreRepository;
import com.example.finalproject12be.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final StoreRepository storeRepository;
    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;

    // 댓글 생성 responseEntity
//    @Transactional
//    public ResponseEntity<CommentResponseDto> createComment(
//            CommentRequestDto commentRequestDto,
//            Member member,
//            Long storeId) {
//        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("Store not found"));
//
//        Comment comment = new Comment(commentRequestDto, store, member);
//        commentRepository.save(comment);
//
//        return ResponseEntity.ok(new CommentResponseDto(comment));
//    }

    @Transactional
    public ResponseMsgDto<CommentResponseDto> createComment(CommentRequestDto commentRequestDto,
                                                            UserDetailsImpl userDetails,
                                                            Long storeId) {
        // 게시글 검증
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        // 댓글 생성
        Comment comment = new Comment(commentRequestDto, store, userDetails.getMember());
        commentRepository.save(comment);

        Optional<Profile> profileOptional = profileRepository.findByMemberId(userDetails.getMember().getId());

        // 응답 생성
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment, profileOptional);
        return ResponseMsgDto.setSuccess(HttpStatus.CREATED.value(), "댓글이 등록되었습니다.", commentResponseDto);
    }

    public ResponseEntity<List<CommentResponseDto>> getComments(Long storeId, UserDetailsImpl userDetails) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        List<Comment> comments = commentRepository.findAllByStoreIdOrderByCreatedAtAsc(storeId);

        List<CommentResponseDto> responseDtos = new ArrayList<>();
        Iterator<Comment> commentIterator = comments.iterator();
        while (commentIterator.hasNext()) {
            Comment comment = commentIterator.next();
            boolean isCurrentUserComment = false;
            if (userDetails != null && userDetails.getMember() != null) {
                isCurrentUserComment = comment.getMember() != null && comment.getMember().getId().equals(userDetails.getMember().getId());
            }

            Optional<Profile> profileOptional = Optional.empty();
            if (comment.getMember() != null) {
                profileOptional = profileRepository.findByMemberId(comment.getMember().getId());
            }

            Member member;
            String nickname;
            if (comment.getMember() == null) {
                member = new Member();
                member.setId(null);
                nickname = "(알수없음)";
            } else {
                member = comment.getMember();
                Optional<Member> memberOptional = memberRepository.findNicknameById(member.getId());
                nickname = memberOptional.map(Member::getNickname).orElse("");
            }

            CommentResponseDto responseDto = new CommentResponseDto(comment, isCurrentUserComment, member, profileOptional);
            responseDto.setNickname(nickname);

            boolean isForeignLanguageStore = store.getForeignLanguage() != null && store.getForeignLanguage() == 1;
            responseDto.setForeign(isForeignLanguageStore);

            responseDtos.add(responseDto);
        }

        return ResponseEntity.ok(responseDtos);
    }

    public List<CommentResponseDto> getUserComments(UserDetailsImpl userDetails) {
        Long memberId = userDetails.getMember().getId();
        List<Comment> comments = commentRepository.findByMemberId(memberId);

        List<CommentResponseDto> responseDtos = new ArrayList<>();
        Iterator<Comment> commentIterator = comments.iterator();
        while (commentIterator.hasNext()) {
            Comment comment = commentIterator.next();
            boolean isCurrentUserComment = comment.getMember().getId().equals(userDetails.getMember().getId());
            Store store = comment.getStore(); // 댓글이 속한 상점 객체 가져오기
            Optional<Profile> profileOptional = profileRepository.findByMemberId(userDetails.getMember().getId());

            Optional<Member> memberOptional = memberRepository.findNicknameById(comment.getMember().getId());
            String nickname = memberOptional.map(Member::getNickname).orElse("");

            CommentResponseDto responseDto = new CommentResponseDto(comment, isCurrentUserComment, store, profileOptional);
            responseDto.setNickname(nickname);

            boolean isForeignLanguageStore = store.getForeignLanguage() != null && store.getForeignLanguage() == 1;
            responseDto.setForeign(isForeignLanguageStore);

            responseDtos.add(responseDto);
        }

        return responseDtos;
    }
    // 댓글 수정
    @Transactional
    public ResponseEntity<CommentResponseDto> updateComment(
            Long commentId,
            CommentRequestDto commentRequestDto,
            UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findByIdAndMemberId(commentId,userDetails.getMember().getId())
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        Optional<Profile> profileOptional = profileRepository.findByMemberId(userDetails.getMember().getId());
        comment.setContents(commentRequestDto.getContents());
        Comment updatedComment = commentRepository.save(comment);

        return ResponseEntity.ok(new CommentResponseDto(updatedComment, profileOptional));
    }

    // 댓글 삭제
    @Transactional
    public ResponseEntity<Void> deleteComment(
            Long commentId,
            Member member) {
        Comment comment = commentRepository.findByIdAndMemberId(commentId, member.getId())
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        commentRepository.delete(comment);

        return ResponseEntity.ok().build();
    }
}