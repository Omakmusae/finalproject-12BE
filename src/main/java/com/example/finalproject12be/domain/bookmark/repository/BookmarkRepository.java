package com.example.finalproject12be.domain.bookmark.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.finalproject12be.domain.bookmark.entity.Bookmark;
import com.example.finalproject12be.domain.member.entity.Member;
import com.example.finalproject12be.domain.store.entity.Store;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

	Optional<Bookmark> findByStoreAndMember(Store store, Member member);

	List<Bookmark> findAllByMember(Member member);
}
