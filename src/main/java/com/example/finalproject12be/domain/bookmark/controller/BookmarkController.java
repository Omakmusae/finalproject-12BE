package com.example.finalproject12be.domain.bookmark.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.finalproject12be.domain.bookmark.dto.BookmarkResponseDto;
import com.example.finalproject12be.domain.bookmark.service.BookmarkService;
import com.example.finalproject12be.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

	private final BookmarkService bookmarkService;

	//북마크 누르기
	@PostMapping("/api/bookmark/{store-id}")
	public void bookmarkStore(@PathVariable(name = "store-id") Long storeId,
		@AuthenticationPrincipal UserDetailsImpl userDetails){ //TODO: User 매개변수 추가
		bookmarkService.bookmarkStore(storeId, userDetails.getMember());
	}

	//마이페이지에서 북마크 확인하기
	@GetMapping("/api/bookmark")
	public List<BookmarkResponseDto> getBookmark(@AuthenticationPrincipal UserDetailsImpl userDetails){
		return bookmarkService.getBookmark(userDetails.getMember());
	}
}
