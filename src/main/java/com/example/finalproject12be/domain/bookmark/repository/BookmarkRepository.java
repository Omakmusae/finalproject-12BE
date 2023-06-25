package com.example.finalproject12be.domain.bookmark.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.finalproject12be.domain.bookmark.entity.Bookmark;
import com.example.finalproject12be.domain.member.entity.Member;
import com.example.finalproject12be.domain.store.entity.Store;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

	Optional<Bookmark> findByStoreAndMember(Store store, Member member);

	@Query("SELECT b " +
		"FROM Bookmark b " +
		"LEFT JOIN FETCH b.store " +
		"LEFT JOIN FETCH b.member " +
		"m WHERE m = :member")// m WHERE m = :member")
	List<Bookmark> findAllWithStore(@Param("member") Member member);

	@Query("SELECT b.store.id FROM Bookmark b WHERE b.member = :member")
	List<Long> findStoreIdsByMember(@Param("member") Member member);
}
