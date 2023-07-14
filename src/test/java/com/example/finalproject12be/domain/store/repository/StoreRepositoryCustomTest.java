package com.example.finalproject12be.domain.store.repository;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestPropertySource;

import com.example.finalproject12be.TestConfig;
import com.example.finalproject12be.domain.bookmark.repository.BookmarkRepository;
import com.example.finalproject12be.domain.store.dto.ForeignStoreResponse;
import com.example.finalproject12be.domain.store.dto.MappedSearchForeignRequest;
import com.example.finalproject12be.security.UserDetailsImpl;

@TestPropertySource(locations = "/application.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestConfig.class})
class StoreRepositoryCustomTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private StoreRepositoryCustom testRepository;

	@Autowired
	private BookmarkRepository bookmarkRepository;



	@Test
	void searchForeignStoreWithFilter() {

		Integer page = 0;
		Integer size = 10;
		String storeName = "";
		String gu = "강남구";
		boolean open = true;
		boolean holidayBusiness = false;
		boolean nightBusiness =false;

		boolean english = false;
		boolean chinese = false;
		boolean japanese = false;

		String radius = "1";
		String latitude = "";
		String longitude = "";

		UserDetailsImpl userDetails = null;

		System.out.println("테스트를 위해 필터 조건을 입력해주세요");
		MappedSearchForeignRequest searchForeignRequest = MappedSearchForeignRequest.builder()
			.page(page)
			.size(size)
			.storeName(storeName)
			.gu(gu)
			.open(open)
			.holidayBusiness(holidayBusiness)
			.nightBusiness(nightBusiness)

			.english(1)
			.chinese(0)
			.japanese(0)
			.baseRadius(radius)
			.baseLatitude(latitude)
			.baseLongitude(longitude)
			.build();

		Page<ForeignStoreResponse> result = testRepository.searchForeignStoreWithFilter(searchForeignRequest, userDetails);

		for (ForeignStoreResponse foreignStoreResponse : result) {
			System.out.println("store.getStoreName = " + foreignStoreResponse.getName());
			System.out.println("store.getAddress = " + foreignStoreResponse.getAddress());

		}

	}
}