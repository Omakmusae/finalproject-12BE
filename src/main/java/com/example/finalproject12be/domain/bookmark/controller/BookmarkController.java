package com.example.finalproject12be.domain.bookmark.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.finalproject12be.domain.bookmark.service.BookmarkService;
import com.example.finalproject12be.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

	private final BookmarkService bookmarkService;

	@PostMapping("/api/bookmark/{store-id}")
	public void bookmarkStore(@PathVariable(name = "store-id") Long storeId,
		@AuthenticationPrincipal UserDetailsImpl userDetails){ //TODO: User 매개변수 추가
		bookmarkService.bookmarkStore(storeId, 1, userDetails.getMember()); //테스트 용도로 userId 하드 코딩, TODO: User 인자 추가
	}
}
