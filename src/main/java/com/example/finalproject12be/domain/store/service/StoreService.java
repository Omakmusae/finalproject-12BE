package com.example.finalproject12be.domain.store.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.finalproject12be.domain.bookmark.entity.Bookmark;
import com.example.finalproject12be.domain.member.entity.Member;
import com.example.finalproject12be.domain.store.dto.OneStoreResponseDto;
import com.example.finalproject12be.domain.store.dto.StoreResponseDto;
import com.example.finalproject12be.domain.store.entity.Store;
import com.example.finalproject12be.domain.store.repository.StoreRepository;
import com.example.finalproject12be.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;

	//약국 전체보기
	public List<StoreResponseDto> getAllStores(UserDetailsImpl userDetails) {

		List<Store> stores = storeRepository.findAll();
		List<StoreResponseDto> storeResponseDtos = new ArrayList<>();

		//로그인한 유저인지 확인
		if(userDetails != null){

			//북마크를 눌렀는지 안 눌렀는지 확인
			Member member = userDetails.getMember();
			storeResponseDtos = checkBookmark(stores, storeResponseDtos, member);

		}else{ //로그인 하지 않은 유저일시 별다른 확인 없이 dto로 감싸 반환

			for(Store store : stores){
				storeResponseDtos.add(new StoreResponseDto(store));
			}

		}

		return storeResponseDtos;
	}

	//북마크 눌렀는지 안 눌렀는지 확인
	private List<StoreResponseDto> checkBookmark(List<Store> stores, List<StoreResponseDto> storeResponseDtos, Member member){

		//북마크를 누른 약국이면 1, 그렇지 않으면 0
		int check = 0;

		for(Store store : stores){

			//북마크가 한 개라도 있는 약국일시
			if(store.getBookmarks().size() != 0){

				//해당 약국의 북마크 배열을 가져옴
				List<Bookmark> bookmarks = store.getBookmarks();

				//요청한 클라이언트가 북마크를 눌렀는지 검사
				for(Bookmark bookmark : bookmarks){
					if(bookmark.getMember().getId() == member.getId()){
						check = 1;
					}

				}
			}

			StoreResponseDto storeResponseDto = new StoreResponseDto(store);

			//북마크를 눌렀다면 dto에 북마크를 true로 세팅
			if(check == 1){
				storeResponseDto.setBookmark(true);
			}

			//dto 배열에 추가하고 체크 0으로 초기화
			storeResponseDtos.add(storeResponseDto);
			check = 0;
		}

		return storeResponseDtos;
	}


	//약국 검색
	public List<StoreResponseDto> searchStore(String storeName, String gu, boolean open, boolean holidayBusiness, boolean nightBusiness, UserDetailsImpl userDetails) {

		int progress = 0; //stores 리스트가 null일 때 0, 반대는 1
		List<StoreResponseDto> storeResponseDtos = new ArrayList<>();
		List<Store> stores = new ArrayList<>();

		//storeName
		if (storeName != null) {
			//findByStoreName
			progress = 1;
			stores = storeRepository.findAllByNameContaining(storeName);
		}

		//gu
		if (gu != null) {

			if (progress == 0) { //저장된 stores가 없을 때

				progress = 1;
				stores = storeRepository.findAllByAddressContaining(gu);

			} else { //저장된 stores가 있을 때

				List<Store> testStores = new ArrayList<>();
				for (Store store : stores) {
					testStores.add(store);
				}

				for (Store testStore : testStores) {
					if (!testStore.getAddress().contains(gu)) {
						stores.remove(testStore);
					}
				}
			}
		}

		//filter
		//영업중
		if (open == true) {

			if (progress == 1) { //저장된 stores가 있을 때만 실행 가능함
				stores = openCheck(stores);
			}

			//공휴일 영업
		} else if (holidayBusiness == true) {

			if (progress == 0) {
				progress = 1;

				stores = storeRepository.findAllByHolidayTimeIsNotNull();
			} else {

				List<Store> testStores = new ArrayList<>();
				for (Store store : stores) {
					testStores.add(store);
				}

				for (Store testStore : testStores) {
					if (testStore.getHolidayTime() == null) {
						stores.remove(testStore);
					}
				}

			}


			//야간 영업
		} else if (nightBusiness == true) {

			if (progress == 0) {

				progress = 1;

				List<Store> newStores = new ArrayList<>();

				stores = storeRepository.findAllByWeekdaysTimeContaining("24");
				newStores = storeRepository.findAllByWeekdaysTimeContaining("25");
				for (Store store : newStores) {
					stores.add(store);
				}

				newStores = storeRepository.findAllByWeekdaysTimeContaining("26");
				for (Store store : newStores) {
					stores.add(store);
				}

				newStores = storeRepository.findAllByWeekdaysTimeContaining("27");
				for (Store store : newStores) {
					stores.add(store);
				}

				newStores = storeRepository.findAllBysaturdayTimeContaining("24");
				for (Store store : newStores) {
					stores.add(store);
				}

				newStores = storeRepository.findAllBysaturdayTimeContaining("25");
				for (Store store : newStores) {
					stores.add(store);
				}

				newStores = storeRepository.findAllBysaturdayTimeContaining("26");
				for (Store store : newStores) {
					stores.add(store);
				}

				newStores = storeRepository.findAllBysaturdayTimeContaining("27");
				for (Store store : newStores) {
					stores.add(store);
				}

				newStores = storeRepository.findAllBysundayTimeContaining("24");
				for (Store store : newStores) {
					stores.add(store);
				}

				newStores = storeRepository.findAllBysundayTimeContaining("25");
				for (Store store : newStores) {
					stores.add(store);
				}

				newStores = storeRepository.findAllBysundayTimeContaining("26");
				for (Store store : newStores) {
					stores.add(store);
				}

				newStores = storeRepository.findAllBysundayTimeContaining("27");
				for (Store store : newStores) {
					stores.add(store);
				}

				newStores = storeRepository.findAllBysundayTimeContaining("28");
				for (Store store : newStores) {
					stores.add(store);
				}

				newStores = storeRepository.findAllBysundayTimeContaining("29");
				for (Store store : newStores) {
					stores.add(store);
				}

			} else if (progress == 1) {

				List<Store> testStores = new ArrayList<>();
				for (Store store : stores) {
					testStores.add(store);
				}

				for (Store testStore : testStores) {

					if (testStore.getWeekdaysTime() != null) {

						String storeTime = testStore.getWeekdaysTime();

						String[] storeTimes = storeTime.split("~");
						String closeHour = storeTimes[1].substring(0, 2);

						if (closeHour.equals("24")) {
							continue;
						} else if (closeHour.equals("25")) {
							continue;
						} else if (closeHour.equals("26")) {
							continue;
						} else if (closeHour.equals("27")) {
							continue;
						} else if (closeHour.equals("28")) {
							continue;
						} else if (closeHour.equals("29")) {
							continue;
						} else if (closeHour.equals("30")) {
							continue;
						} else {
							stores.remove(testStore);
						}

					}

					if (testStore.getSaturdayTime() != null) {

						String storeTime = testStore.getSaturdayTime();

						String[] storeTimes = storeTime.split("~");
						String closeHour = storeTimes[1].substring(0, 2);

						if (closeHour.equals("24")) {
							continue;
						} else if (closeHour.equals("25")) {
							continue;
						} else if (closeHour.equals("26")) {
							continue;
						} else if (closeHour.equals("27")) {
							continue;
						} else if (closeHour.equals("28")) {
							continue;
						} else if (closeHour.equals("29")) {
							continue;
						} else if (closeHour.equals("30")) {
							continue;
						} else {
							stores.remove(testStore);
						}

					}

					if (testStore.getSundayTime() != null) {

						String storeTime = testStore.getSundayTime();

						String[] storeTimes = storeTime.split("~");
						String closeHour = storeTimes[1].substring(0, 2);

						if (closeHour.equals("24")) {
							continue;
						} else if (closeHour.equals("25")) {
							continue;
						} else if (closeHour.equals("26")) {
							continue;
						} else if (closeHour.equals("27")) {
							continue;
						} else if (closeHour.equals("28")) {
							continue;
						} else if (closeHour.equals("29")) {
							continue;
						} else if (closeHour.equals("30")) {
							continue;
						} else {
							stores.remove(testStore);
						}

					}

				}
			}
		}

		//로그인한 사용자일시 북마크 눌렀는지 안 눌렀는지 반환
		if (userDetails != null) {

			Member member = userDetails.getMember();
			storeResponseDtos = checkBookmark(stores, storeResponseDtos, member);

		} else {

			for (Store store : stores) {
				storeResponseDtos.add(new StoreResponseDto(store));
			}
		}

		return storeResponseDtos;
	}

	//약국 상세조회
	public OneStoreResponseDto getStore(Long storeId, UserDetailsImpl userDetails) {
		Store store = storeRepository.findById(storeId)
				.orElseThrow(() -> new IllegalArgumentException("해당 약국은 존재하지 않습니다."));

		if(userDetails != null){
			Member member = userDetails.getMember();

			if(store.getBookmarks().size() != 0){
				List<Bookmark> bookmarks = store.getBookmarks();

				for(Bookmark bookmark : bookmarks){
					if(bookmark.getMember().getId() == member.getId()){

						OneStoreResponseDto oneStoreResponseDto = new OneStoreResponseDto(store);
						oneStoreResponseDto.setBookmark(true);
						oneStoreResponseDto.setTotalBookmark(store.getBookmarks().size());
						return oneStoreResponseDto;

					}

				}
			}

		}
		return new OneStoreResponseDto(store);
	}

	//영업 중 필터 검사
	private List<Store> openCheck(List<Store> stores){

		//test
		List<Store> testStores = new ArrayList<>();
		for(Store store: stores){
			testStores.add(store);
		}

		LocalDate now = LocalDate.now();
		int dayOfWeek = now.getDayOfWeek().getValue();

		LocalTime nowTime = LocalTime.now();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

		// 포맷 적용하기
		String formatedNow = nowTime.format(formatter);
		int nowHour = Integer.parseInt(formatedNow.substring(0, 2));
		int nowMin = Integer.parseInt(formatedNow.substring(3, 5));

		int openHour = 0;
		int openMin = 0;
		int closeHour = 0;
		int closeMin = 0;

		for (Store testStore : testStores) {

			int status = 0;

			if (dayOfWeek > 0 && dayOfWeek < 6) { //평일

				String storeTime = testStore.getWeekdaysTime();

				if(storeTime != null){
					String[] storeTimes = storeTime.split("~");

					openHour = Integer.parseInt(storeTimes[0].substring(3, 5));
					openMin = Integer.parseInt(storeTimes[0].substring(6));

					closeHour = Integer.parseInt(storeTimes[1].substring(0, 2));
					closeMin = Integer.parseInt(storeTimes[1].substring(3));
				}else {

					status = 1;
					stores.remove(testStore);

				}
			}else if (dayOfWeek == 6){ // 토요일 TODO: 일요일이랑 합치기

				String storeTime = testStore.getWeekdaysTime();

				if (storeTime != null){
					String[] storeTimes = storeTime.split("~");

					openHour = Integer.parseInt(storeTimes[0].substring(2, 4));
					openMin = Integer.parseInt(storeTimes[0].substring(5));

					closeHour = Integer.parseInt(storeTimes[1].substring(0, 2));
					closeMin = Integer.parseInt(storeTimes[1].substring(3));
				}else{
					status = 1;
					stores.remove(testStore);
				}



			}else if( dayOfWeek == 7){ // 일요일

				String storeTime = testStore.getWeekdaysTime();

				if(storeTime != null){
					String[] storeTimes = storeTime.split("~");

					openHour = Integer.parseInt(storeTimes[0].substring(2, 4));
					openMin = Integer.parseInt(storeTimes[0].substring(5));

					closeHour = Integer.parseInt(storeTimes[1].substring(0, 2));
					closeMin = Integer.parseInt(storeTimes[1].substring(3));
				}else {

					status = 1;
					stores.remove(testStore);
				}
			}

			if(status != 1){
				if(nowHour < 5){
					nowHour = nowHour + 24;
				}

				if((openHour > nowHour) || (closeHour < nowHour)){
					stores.remove(testStore);
				}else if(openHour == nowHour && openMin > nowMin){
					stores.remove(testStore);
				} else if(closeHour == nowHour && closeMin < nowMin){ //현재 시간이 영업 시간에 포함되지 않을 때
					stores.remove(testStore);
				}
			}
		}

		return stores;

	}

}