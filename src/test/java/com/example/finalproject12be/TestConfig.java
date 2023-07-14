package com.example.finalproject12be;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.example.finalproject12be.domain.bookmark.repository.BookmarkRepository;
import com.example.finalproject12be.domain.store.repository.StoreRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;

@TestConfiguration
public class TestConfig {

	@PersistenceContext
	private EntityManager entityManager;

	@Bean
	public JPAQueryFactory jpaQueryFactory() {
		return new JPAQueryFactory(entityManager);
	}

	public BookmarkRepository bookmarkRepository;

	@Bean
	public StoreRepositoryCustom testRepository(JPAQueryFactory jpaQueryFactory) {
		return new StoreRepositoryCustom(jpaQueryFactory, bookmarkRepository);
	}

}