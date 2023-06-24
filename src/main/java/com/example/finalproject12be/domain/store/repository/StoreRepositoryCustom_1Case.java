package com.example.finalproject12be.domain.store.repository;

import static com.example.finalproject12be.domain.store.entity.QStore.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.example.finalproject12be.domain.bookmark.repository.BookmarkRepository;
import com.example.finalproject12be.domain.member.entity.Member;
import com.example.finalproject12be.domain.store.dto.ForeignStoreResponse;
import com.example.finalproject12be.domain.store.dto.MappedSearchForeignRequest;
import com.example.finalproject12be.domain.store.dto.MappedSearchRequest;
import com.example.finalproject12be.domain.store.dto.StoreResponseDto;
import com.example.finalproject12be.security.UserDetailsImpl;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryCustom_1Case {

	private final JPAQueryFactory jpaQueryFactory;
	private final BookmarkRepository bookmarkRepository;

	public Page<StoreResponseDto> searchStoreWithFilter(MappedSearchRequest request, UserDetailsImpl userDetails) {

		int page = request.getPage();
		int size = request.getSize();
		NumberExpression<Double> distance = distance(request.getLatitude(), request.getLongitude(), store.latitude, store.longitude);

		if (distance == null) {

			QueryResults<StoreResponseDto> results = jpaQueryFactory
				.select(Projections.constructor(StoreResponseDto.class,
					store.id, store.address, store.name, store.callNumber,
					store.weekdaysTime, store.longitude, store.latitude))
				.from(store)
				.where(
					eqAddress(request.getGu()),
					eqStoreName(request.getStoreName()),
					checkOpen(request.isOpen()),
					checkHolidayOpen(request.isHolidayBusiness()),
					checkNightdOpen(request.isNightBusiness())
				)
				.orderBy(store.name.asc())
				.offset(page * size)
				.limit(size)
				.fetchResults();

			// 로그인한 유저의 북마크 정보 가져오기
			List<Long> bookmarkedStoreIds = new ArrayList<>();

			if (userDetails != null) {
				Member member = userDetails.getMember();
				bookmarkedStoreIds = bookmarkRepository.findStoreIdsByMember(member);
			}

			List<StoreResponseDto> storeResponseDtos = results.getResults();

			// 북마크 여부 체크하여 StoreResponseDto에 설정
			for (StoreResponseDto result : storeResponseDtos) {
				if (bookmarkedStoreIds.contains(result.getStoreId())) {
					result.setBookmark(true);
				}
			}


			return new PageImpl<>(results.getResults(), PageRequest.of(page, size), results.getTotal());
		}
		else {
			QueryResults<StoreResponseDto> results = jpaQueryFactory
				.select(Projections.constructor(StoreResponseDto.class,
					store.id, store.address, store.name, store.callNumber,
					store.weekdaysTime, store.longitude, store.latitude))
				.from(store)
				.where(
					withinDistance(request.getLatitude(), request.getLongitude(), store.latitude, store.longitude),
					eqAddress(request.getGu()),
					eqStoreName(request.getStoreName()),
					checkOpen(request.isOpen()),
					checkHolidayOpen(request.isHolidayBusiness()),
					checkNightdOpen(request.isNightBusiness())
				)
				.orderBy(distance.asc(), store.name.asc())
				.offset(page * size)
				.limit(size)
				.fetchResults();

			// 로그인한 유저의 북마크 정보 가져오기
			List<Long> bookmarkedStoreIds = new ArrayList<>();

			if (userDetails != null) {
				Member member = userDetails.getMember();
				bookmarkedStoreIds = bookmarkRepository.findStoreIdsByMember(member);
			}

			List<StoreResponseDto> storeResponseDtos = results.getResults();

			// 북마크 여부 체크하여 StoreResponseDto에 설정
			for (StoreResponseDto result : storeResponseDtos) {
				if (bookmarkedStoreIds.contains(result.getStoreId())) {
					result.setBookmark(true);
				}
			}

			return new PageImpl<>(results.getResults(), PageRequest.of(page, size), results.getTotal());
		}

	}


	public Page<ForeignStoreResponse> searchForeignStoreWithFilter(MappedSearchForeignRequest request, UserDetailsImpl userDetails) {

		int page = request.getPage();
		int size = request.getSize();
		NumberExpression<Double> distance = distance(request.getLatitude(), request.getLongitude(), store.latitude, store.longitude);

		if (distance == null) {
			QueryResults<ForeignStoreResponse> results = jpaQueryFactory
				.select(Projections.constructor(
					ForeignStoreResponse.class,
					store.id, store.address, store.name, store.callNumber,
					store.weekdaysTime, store.longitude, store.latitude,
					store.english, store.chinese, store.japanese))
				.from(store)
				.where(
					eqAddress(request.getGu()),
					eqStoreName(request.getStoreName()),
					checkOpen(request.isOpen()),
					checkHolidayOpen(request.isHolidayBusiness()),
					checkNightdOpen(request.isNightBusiness()),
					eqEnglish(request.getEnglish()),
					eqChinese(request.getChinese()),
					eqJapanese(request.getJapanese())
				)
				.orderBy(store.name.asc())
				.offset(page * size)
				.limit(size)
				.fetchResults();
			// 로그인한 유저의 북마크 정보 가져오기
			List<Long> bookmarkedStoreIds = new ArrayList<>();

			if (userDetails != null) {
				Member member = userDetails.getMember();
				bookmarkedStoreIds = bookmarkRepository.findStoreIdsByMember(member);
			}

			List<ForeignStoreResponse> foreignStoreResponses = results.getResults();

			// 북마크 여부 체크하여 StoreResponseDto에 설정
			for (ForeignStoreResponse result : foreignStoreResponses) {
				if (bookmarkedStoreIds.contains(result.getStoreId())) {
					result.setBookmark(true);
				}
			}

			return new PageImpl<>(results.getResults(), PageRequest.of(page, size), results.getTotal());
		}
		else {
			QueryResults<ForeignStoreResponse> results = jpaQueryFactory
				.select(Projections.constructor(
					ForeignStoreResponse.class,
					store.id, store.address, store.name, store.callNumber,
					store.weekdaysTime, store.longitude, store.latitude,
					store.english, store.chinese, store.japanese))
				.from(store)
				.where(
					withinDistance(request.getLatitude(), request.getLongitude(), store.latitude, store.longitude),
					eqAddress(request.getGu()),
					eqStoreName(request.getStoreName()),
					checkOpen(request.isOpen()),
					checkHolidayOpen(request.isHolidayBusiness()),
					checkNightdOpen(request.isNightBusiness()),
					eqEnglish(request.getEnglish()),
					eqChinese(request.getChinese()),
					eqJapanese(request.getJapanese())
				)
				.orderBy(distance.asc(), store.name.asc())
				.offset(page * size)
				.limit(size)
				.fetchResults();
			// 로그인한 유저의 북마크 정보 가져오기
			List<Long> bookmarkedStoreIds = new ArrayList<>();

			if (userDetails != null) {
				Member member = userDetails.getMember();
				bookmarkedStoreIds = bookmarkRepository.findStoreIdsByMember(member);
			}

			List<ForeignStoreResponse> foreignStoreResponses = results.getResults();

			// 북마크 여부 체크하여 StoreResponseDto에 설정
			for (ForeignStoreResponse result : foreignStoreResponses) {
				if (bookmarkedStoreIds.contains(result.getStoreId())) {
					result.setBookmark(true);
				}
			}

			return new PageImpl<>(results.getResults(), PageRequest.of(page, size), results.getTotal());
		}

	}



	private BooleanExpression eqAddress(String address) {

		return address != null ? store.address.like("%" + address + "%") : null;
	}

	private BooleanExpression eqStoreName(String storeName) {

		return storeName != null ? store.name.like("%" + storeName + "%") : null;
	}

	private BooleanExpression checkOpen(boolean open) {
		if (open) {
			// 현재 시간이 주중 영업 시간 내에 있는지 체크
			return Expressions.booleanTemplate(
				// "(WEEKDAY(CONVERT_TZ(NOW(), '+00:00', '+09:00')) BETWEEN 0 AND 4 " +
				// "AND TIME_FORMAT(CONVERT_TZ(NOW(), '+00:00', '+09:00'), '%H:%i') <= TIME_FORMAT(SUBSTRING_INDEX(weekdays_time, ' ', -1), '%H:%i') " +
				// "AND TIME_FORMAT(CONVERT_TZ(NOW(), '+00:00', '+09:00'), '%H:%i') >= TIME_FORMAT(SUBSTRING_INDEX(SUBSTRING_INDEX(weekdays_time, ' ~', 1), ' ', -1), '%H:%i')) " +
				//
				// "OR (WEEKDAY(CONVERT_TZ(NOW(), '+00:00', '+09:00')) = 5 " +
				// "AND TIME_FORMAT(CONVERT_TZ(NOW(), '+00:00', '+09:00'), '%H:%i') <= TIME_FORMAT(SUBSTRING_INDEX(saturday_time, ' ', -1), '%H:%i') " +
				// "AND TIME_FORMAT(CONVERT_TZ(NOW(), '+00:00', '+09:00'), '%H:%i') >= TIME_FORMAT(SUBSTRING_INDEX(SUBSTRING_INDEX(saturday_time, ' ~', 1), ' ', -1), '%H:%i')) " +
				//
				// "OR (WEEKDAY(CONVERT_TZ(NOW(), '+00:00', '+09:00')) = 6 " +
				// "AND TIME_FORMAT(CONVERT_TZ(NOW(), '+00:00', '+09:00'), '%H:%i') <= TIME_FORMAT(SUBSTRING_INDEX(sunday_time, ' ', -1), '%H:%i') " +
				// "AND TIME_FORMAT(CONVERT_TZ(NOW(), '+00:00', '+09:00'), '%H:%i') >= TIME_FORMAT(SUBSTRING_INDEX(SUBSTRING_INDEX(sunday_time, ' ~', 1), ' ', -1), '%H:%i'))"

				"(WEEKDAY(NOW()) BETWEEN 0 AND 4 AND TIME(NOW()) <= TIME_FORMAT(SUBSTRING_INDEX(weekdays_time, ' ', -1), '%H:%i') " +
					"AND TIME(NOW()) >= TIME_FORMAT(SUBSTRING_INDEX(SUBSTRING_INDEX(weekdays_time, ' ~', 1), ' ', -1), '%H:%i')) " +
					"OR (WEEKDAY(NOW()) = 5 AND TIME(NOW()) <= TIME_FORMAT(SUBSTRING_INDEX(saturday_time, ' ', -1), '%H:%i') " +
					"AND TIME(NOW()) >= TIME_FORMAT(SUBSTRING_INDEX(SUBSTRING_INDEX(saturday_time, ' ~', 1), ' ', -1), '%H:%i')) " +
					"OR (WEEKDAY(NOW()) = 6 AND TIME(NOW()) <= TIME_FORMAT(SUBSTRING_INDEX(sunday_time, ' ', -1), '%H:%i') " +
					"AND TIME(NOW()) >= TIME_FORMAT(SUBSTRING_INDEX(SUBSTRING_INDEX(sunday_time, ' ~', 1), ' ', -1), '%H:%i'))"
			);
		} else {
			// 영업 시간과 관계없이 모든 가게 조회
			return null;
		}
	}

	private BooleanExpression checkHolidayOpen(boolean holidayBusiness) {
		return holidayBusiness == true ? store.holidayTime.isNotNull() : null;
	}

	private BooleanExpression checkNightdOpen(boolean nightBusiness) {
		return nightBusiness == true ? store.nightPharmacy.isNotNull() : null;
	}

	private BooleanExpression eqEnglish(int english) {

		return english == 1 ? store.english.eq(1) : null;
	}

	private BooleanExpression eqChinese(Integer chinese) {
		return chinese == 1 ? store.chinese.eq(1) : null;
	}

	private BooleanExpression eqJapanese(Integer japanese) {
		return japanese == 1 ? store.japanese.eq(1) : null;
	}

	private BooleanExpression withinDistance(String baseLatitude, String baseLongitude, NumberPath<Double> latitude, NumberPath<Double> longitude) {
		if (baseLatitude == null) {

			return null;}
		else {
			// Double b_latitude = Double.parseDouble(baseLatitude);
			// Double b_longitude = Double.parseDouble(baseLongitude);
			Double baseRadius = 1.0;

			NumberExpression<Double> distance = distance(baseLatitude, baseLongitude, latitude, longitude);
			return distance.loe(baseRadius);
		}
	}

	private NumberExpression<Double> distance(String baseLatitude, String baseLongitude, NumberPath<Double> latitude, NumberPath<Double> longitude) {

		if (baseLatitude == null) {
			return null;
		} else {
			double earthRadius = 6371; // 지구 반지름 (단위: km)
			double baseLatitudeRad = Math.toRadians(Double.parseDouble(baseLatitude));
			double baseLongitudeRad = Math.toRadians(Double.parseDouble(baseLongitude));

			return Expressions.numberTemplate(Double.class,
				"({0} * acos(cos({1}) * cos(radians({3})) * cos(radians({4})-{2}) + sin({1}) * sin(radians({3}))))",
				earthRadius, baseLatitudeRad, baseLongitudeRad, latitude, longitude);
		}
	}


}
