package com.example.finalproject12be.domain.bookmark.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.finalproject12be.domain.bookmark.dto.BookmarkResponseDto;
import com.example.finalproject12be.domain.bookmark.entity.Bookmark;
import com.example.finalproject12be.domain.bookmark.repository.BookmarkRepository;
import com.example.finalproject12be.domain.member.entity.Member;
import com.example.finalproject12be.domain.store.entity.Store;
import com.example.finalproject12be.domain.store.repository.StoreRepository;
import com.example.finalproject12be.exception.CommonErrorCode;
import com.example.finalproject12be.exception.RestApiException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkService {

	private final BookmarkRepository bookmarkRepository;
	private final StoreRepository storeRepository;

	//북마크 누르기
	@Transactional
	public void bookmarkStore(Long storeId, Member member) {

		//TODO: custom Exception 사용하기
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));

		Optional<Bookmark> bookmarkOptional = bookmarkRepository.findByStoreAndMember(store, member);

		if (bookmarkOptional.isPresent()) {
			Bookmark bookmark = bookmarkOptional.get();

			//(북마크 포함) 연관된 entity에서 북마크 삭제 로직
			bookmarkRepository.delete(bookmark);
			member.deleteBookmark(bookmark);
			store.deleteBookmark(bookmark);
		} else {
			Bookmark bookmark = new Bookmark(store, member);

			//(북마크 포함) 연관된 entity에서 북마크 저장 로직
			bookmarkRepository.save(bookmark);
			member.addBookmark(bookmark);
			store.addBookmark(bookmark);
		}

	}

	//마이페이지에서 북마크 확인하기
	//TODO n+1 잡기
	public List<BookmarkResponseDto> getBookmark(Member member) {

		List<Bookmark> bookmarks = bookmarkRepository.findAllWithStore(member);
		List<BookmarkResponseDto> bookmarkResponseDtos = new ArrayList<>();

		Iterator<Bookmark> bookmarkIterator = bookmarks.iterator();
		while (bookmarkIterator.hasNext()) {

			Bookmark bookmark = bookmarkIterator.next();
			Store store = bookmark.getStore();
			BookmarkResponseDto bookmarkResponseDto = new BookmarkResponseDto(store);
			bookmarkResponseDtos.add(bookmarkResponseDto);

			if (store.getHolidayTime() != null) {
				bookmarkResponseDto.setHolidayBusiness(true);
			}

			if (store.getNightPharmacy() != 0) {
				bookmarkResponseDto.setNightBusiness(true);
			}

			if (store.getForeignLanguage() != null) {
				bookmarkResponseDto.setForeign(true);
			}
		}

		return bookmarkResponseDtos;
	}
}
