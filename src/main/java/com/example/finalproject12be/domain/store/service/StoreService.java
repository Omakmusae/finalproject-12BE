package com.example.finalproject12be.domain.store.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.finalproject12be.domain.bookmark.entity.Bookmark;
import com.example.finalproject12be.domain.member.entity.Member;
import com.example.finalproject12be.domain.store.dto.ForeignOneStoreResponse;
import com.example.finalproject12be.domain.store.dto.ForeignStoreResponse;
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


	public List<StoreResponseDto> getAllStores(UserDetailsImpl userDetails) {

		List<Store> stores = storeRepository.findAll();
		List<StoreResponseDto> storeResponseDtos = new ArrayList<>();

		if(userDetails != null){
			Member member = userDetails.getMember();
			storeResponseDtos = checkBookmark(stores, storeResponseDtos, member);
		}else{

			for(Store store : stores){
				storeResponseDtos.add(new StoreResponseDto(store));
			}

		}

		return storeResponseDtos;
	}

	private List<ForeignStoreResponse> checkForeignBookmark(List<Store> stores, List<ForeignStoreResponse> foreignStoreResponses, UserDetailsImpl userDetails){

		int bookmarkCheck = 0;
		boolean english = false;
		boolean chinese = false;
		boolean japanese = false;

		for(Store store : stores){

			if(userDetails != null){

				Member member = userDetails.getMember();

				if(store.getBookmarks().size() != 0){
					List<Bookmark> bookmarks = store.getBookmarks();

					for(Bookmark bookmark : bookmarks){
						if(bookmark.getMember().getId() == member.getId()){
							bookmarkCheck = 1;
						}

					}
				}
			}


			if(store.getForeignLanguage() != null){

				if(store.getEnglish() == 1){
					english = true;
				}

				if(store.getChinese() == 1){
					chinese = true;
				}

				if(store.getJapanese() == 1){
					japanese = true;
				}

			}



			ForeignStoreResponse foreignStoreResponse = new ForeignStoreResponse(store);


			if(bookmarkCheck == 1){
				foreignStoreResponse.setBookmark(true);
			}

			foreignStoreResponse.setLanguage(english, chinese, japanese);

			foreignStoreResponses.add(foreignStoreResponse);
			bookmarkCheck = 0;
			english = false;
			chinese = false;
			japanese = false;

		}

		return foreignStoreResponses;

	}

	private List<StoreResponseDto> checkBookmark(List<Store> stores, List<StoreResponseDto> storeResponseDtos, Member member){

		int check = 0;

		for(Store store : stores){

			if(store.getBookmarks().size() != 0){
				List<Bookmark> bookmarks = store.getBookmarks();

				for(Bookmark bookmark : bookmarks){
					if(bookmark.getMember().getId() == member.getId()){
						check = 1;
					}

				}
			}

			StoreResponseDto storeResponseDto = new StoreResponseDto(store);


			if(check == 1){
				storeResponseDto.setBookmark(true);
			}

			storeResponseDtos.add(storeResponseDto);
			check = 0;

		}

		return storeResponseDtos;

	}

	public List<StoreResponseDto> searchStore(String storeName, String gu, boolean open, boolean holidayBusiness, boolean nightBusiness, UserDetailsImpl userDetails) {

		int progress = 0; //stores 리스트가 null일 때 0, 반대는 1
		List<StoreResponseDto> storeResponseDtos = new ArrayList<>();
		List<Store> stores = new ArrayList<>();

		//storeName
		if(storeName != null){
			progress = 1;
			stores = storeRepository.findAllByNameContaining(storeName);
		}

		//gu
		if(gu != null){

			if(progress == 0){ //저장된 stores가 없을 때

				progress = 1;
				stores = storeRepository.findAllByAddressContaining(gu);

			}else{ //저장된 stores가 있을 때

				List<Store> testStores = new ArrayList<>();
				for(Store store: stores){
					testStores.add(store);
				}

				for(Store testStore : testStores){
					if(!testStore.getAddress().contains(gu)){
						stores.remove(testStore);
					}
				}
			}
		} //구가 요청되지 않았을 때는 progress가 0이고 저장될 사항이 없기 때문에 else 생략

		//filter
		if(open == true){ // 영업중 필터

			if(progress == 1){ //저장된 stores가 있을 때만 실행 가능함

				stores = openCheck(stores);

			}

		}else if(holidayBusiness == true){

			if(progress == 0){
				progress = 1;

				stores = storeRepository.findAllByHolidayTimeIsNotNull();
			}else{

				//test
				List<Store> testStores = new ArrayList<>();
				for(Store store: stores){
					testStores.add(store);
				}

				for(Store testStore : testStores){
					if(testStore.getHolidayTime() == null){
						stores.remove(testStore);
					}
				}

			}

		}else if (nightBusiness == true){

			// nightBusinessCheck(stores);

			if(progress == 0){

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
			}else if(progress == 1){

				//test
				List<Store> testStores = new ArrayList<>();
				for(Store store: stores){
					testStores.add(store);
				}

				for(Store testStore : testStores){

					if(testStore.getWeekdaysTime() != null){

						String storeTime = testStore.getWeekdaysTime();

						String[] storeTimes = storeTime.split("~");
						String closeHour = storeTimes[1].substring(0, 2);

						if(closeHour.equals("24")) {
							continue;
						}else if(closeHour.equals("25")){
							continue;
						}else if(closeHour.equals("26")){
							continue;
						}else if(closeHour.equals("27")){
							continue;
						}else if(closeHour.equals("28")){
							continue;
						}else if(closeHour.equals("29")){
							continue;
						}else if(closeHour.equals("30")){
							continue;
						}else{
							stores.remove(testStore);
						}

					}

				if(testStore.getSaturdayTime() != null){

					String storeTime = testStore.getSaturdayTime();

					String[] storeTimes = storeTime.split("~");
					String closeHour = storeTimes[1].substring(0, 2);

					if(closeHour.equals("24")) {
						continue;
					}else if(closeHour.equals("25")){
						continue;
					}else if(closeHour.equals("26")){
						continue;
					}else if(closeHour.equals("27")){
						continue;
					}else if(closeHour.equals("28")){
						continue;
					}else if(closeHour.equals("29")){
						continue;
					}else if(closeHour.equals("30")){
						continue;
					}else{
						stores.remove(testStore);
					}
				}

				if (testStore.getSundayTime() != null){

						String storeTime = testStore.getSundayTime();

						String[] storeTimes = storeTime.split("~");
						String closeHour = storeTimes[1].substring(0, 2);

						if(closeHour.equals("24")) {
							continue;
						}else if(closeHour.equals("25")){
							continue;
						}else if(closeHour.equals("26")){
							continue;
						}else if(closeHour.equals("27")){
							continue;
						}else if(closeHour.equals("28")){
							continue;
						}else if(closeHour.equals("29")){
							continue;
						}else if(closeHour.equals("30")){
							continue;
						}else{
							stores.remove(testStore);
						}
					}
				}
			}
		}
		if(userDetails != null){
			Member member = userDetails.getMember();
			storeResponseDtos = checkBookmark(stores, storeResponseDtos, member);
		}else{

			for(Store store : stores){
				storeResponseDtos.add(new StoreResponseDto(store));
			}

		}

		return storeResponseDtos;
	}

	public OneStoreResponseDto getStore(Long storeId, UserDetailsImpl userDetails) {
		Store store = storeRepository.findById(storeId)
				.orElseThrow(() -> new IllegalArgumentException("해당 약국은 존재하지 않습니다."));

		OneStoreResponseDto oneStoreResponseDto = new OneStoreResponseDto(store);

		if(userDetails != null){
			Member member = userDetails.getMember();

			if(store.getBookmarks().size() != 0){
				List<Bookmark> bookmarks = store.getBookmarks();

				for(Bookmark bookmark : bookmarks){
					if(bookmark.getMember().getId() == member.getId()){


						oneStoreResponseDto.setBookmark(true);
						oneStoreResponseDto.setTotalBookmark(store.getBookmarks().size());


					}

				}

			}

		}

		if(store.getNightPharmacy() == 1){
			oneStoreResponseDto.setNightBusiness(true);
		}

		if(store.getBookmarks().size() != 0){
			oneStoreResponseDto.setTotalBookmark(store.getBookmarks().size());
		}

		return oneStoreResponseDto;
	}

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

					// //test
					// if(storeTimes[0].substring(3, 5).equals("nu")){
					// 	System.out.println("원래 문자열: " + storeTimes[0]);
					// 	System.out.println("두 번째 문자열: " + storeTimes[1]);
					// 	stores.remove(testStore);
					// 	status = 1;
					// }

					openHour = Integer.parseInt(storeTimes[0].substring(3, 5));
					openMin = Integer.parseInt(storeTimes[0].substring(6));

					closeHour = Integer.parseInt(storeTimes[1].substring(0, 2));
					closeMin = Integer.parseInt(storeTimes[1].substring(3));
				}else {

					status = 1;
					stores.remove(testStore);

				}

			}else if (dayOfWeek == 6){ // 토요일 TODO: 일요일이랑 합치기

				String storeTime = testStore.getSaturdayTime();

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

				String storeTime = testStore.getSundayTime();

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

	public List<Store> getLocation(Double baseRadius,Double baseLatitude, Double baseLongitude, String address) {

		List<Store> result = storeRepository.findByDistanceWithinRadius(baseLatitude, baseLongitude, baseRadius, address);
		return result;
	}


	public List<ForeignStoreResponse> searchForeignStore(String storeName, String gu, boolean open, boolean holidayBusiness, boolean nightBusiness, boolean english, boolean chinese, boolean japanese, UserDetailsImpl userDetails) {

		int progress = 0; //stores 리스트가 null일 때 0, 반대는 1
		List<ForeignStoreResponse> foreignStoreResponses = new ArrayList<>();
		List<Store> stores = new ArrayList<>();

		//storeName
		if(!storeName.equals("")){//if(storeName != null){ //TODO: 주석 풀기
			progress = 1;
			stores = storeRepository.findAllByNameContaining(storeName);
		}

		//gu
		if(!gu.equals("")){//if(gu != null){ //TODO: 주석 풀기

			if(progress == 0){ //저장된 stores가 없을 때

				progress = 1;
				stores = storeRepository.findAllByAddressContaining(gu);

			}else{ //저장된 stores가 있을 때

				List<Store> testStores = new ArrayList<>();
				for(Store store: stores){
					testStores.add(store);
				}

				for(Store testStore : testStores){
					if(!testStore.getAddress().contains(gu)){
						stores.remove(testStore);
					}
				}
			}
		} //구가 요청되지 않았을 때는 progress가 0이고 저장될 사항이 없기 때문에 else 생략

		//filter
		if(open == true){ // 영업중 필터

			if(progress == 1){ //저장된 stores가 있을 때만 실행 가능함

				stores = openCheck(stores);

			}

		}else if(holidayBusiness == true){

			if(progress == 0){
				progress = 1;

				stores = storeRepository.findAllByHolidayTimeIsNotNull();
			}else{

				//test
				List<Store> testStores = new ArrayList<>();
				for(Store store: stores){
					testStores.add(store);
				}

				for(Store testStore : testStores){
					if(testStore.getHolidayTime() == null){
						stores.remove(testStore);
					}
				}

			}

		}else if (nightBusiness == true){

			// nightBusinessCheck(stores);

			if(progress == 0){

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
			}else if(progress == 1){

				//test
				List<Store> testStores = new ArrayList<>();
				for(Store store: stores){
					testStores.add(store);
				}

				for(Store testStore : testStores){

					if(testStore.getWeekdaysTime() != null){

						String storeTime = testStore.getWeekdaysTime();

						String[] storeTimes = storeTime.split("~");
						String closeHour = storeTimes[1].substring(0, 2);

						if(closeHour.equals("24")) {
							continue;
						}else if(closeHour.equals("25")){
							continue;
						}else if(closeHour.equals("26")){
							continue;
						}else if(closeHour.equals("27")){
							continue;
						}else if(closeHour.equals("28")){
							continue;
						}else if(closeHour.equals("29")){
							continue;
						}else if(closeHour.equals("30")){
							continue;
						}else{
							stores.remove(testStore);
						}

					}

					if(testStore.getSaturdayTime() != null){

						String storeTime = testStore.getSaturdayTime();

						String[] storeTimes = storeTime.split("~");
						String closeHour = storeTimes[1].substring(0, 2);

						if(closeHour.equals("24")) {
							continue;
						}else if(closeHour.equals("25")){
							continue;
						}else if(closeHour.equals("26")){
							continue;
						}else if(closeHour.equals("27")){
							continue;
						}else if(closeHour.equals("28")){
							continue;
						}else if(closeHour.equals("29")){
							continue;
						}else if(closeHour.equals("30")){
							continue;
						}else{
							stores.remove(testStore);
						}
					}

					if (testStore.getSundayTime() != null){

						String storeTime = testStore.getSundayTime();

						String[] storeTimes = storeTime.split("~");
						String closeHour = storeTimes[1].substring(0, 2);

						if(closeHour.equals("24")) {
							continue;
						}else if(closeHour.equals("25")){
							continue;
						}else if(closeHour.equals("26")){
							continue;
						}else if(closeHour.equals("27")){
							continue;
						}else if(closeHour.equals("28")){
							continue;
						}else if(closeHour.equals("29")){
							continue;
						}else if(closeHour.equals("30")){
							continue;
						}else{
							stores.remove(testStore);
						}
					}
				}
			}
		}

		if(english == true){
			if(progress == 0){
				progress = 1;
				stores = storeRepository.findAllByEnglish(1);
			} else{

				List<Store> testStores = new ArrayList<>();
				for(Store store: stores){
					testStores.add(store);
				}

				for(Store testStore : testStores){

					if(testStore.getEnglish() == null || testStore.getEnglish() == 1){
						stores.remove(testStore);
					}
				}

			}

		}else if(chinese == true){
			if(progress == 0){
				progress = 1;
				stores = storeRepository.findAllByChinese(1);
			} else{

				List<Store> testStores = new ArrayList<>();
				for(Store store: stores){
					testStores.add(store);
				}

				for(Store testStore : testStores){

					if(testStore.getChinese() == null || testStore.getChinese() == 1){
						stores.remove(testStore);
					}
				}

			}

		}else if(japanese == true){
			if(progress == 0){
				progress = 1;
				stores = storeRepository.findAllByJapanese(1);
			} else{

				List<Store> testStores = new ArrayList<>();
				for(Store store: stores){
					testStores.add(store);
				}

				for(Store testStore : testStores){

					if(testStore.getJapanese() == null || testStore.getJapanese() == 1){
						stores.remove(testStore);
					}
				}

			}

		}

		foreignStoreResponses = checkForeignBookmark(stores, foreignStoreResponses, userDetails);

		return foreignStoreResponses;
	}

	//ING
	//외국어 약국 상세보기
	public ForeignOneStoreResponse getForeignStore(Long storeId, UserDetailsImpl userDetails) {

		boolean english = false;
		boolean chinese = false;
		boolean japanese = false;

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new IllegalArgumentException("해당 약국은 존재하지 않습니다."));

		ForeignOneStoreResponse foreignOneStoreResponse = new ForeignOneStoreResponse(store);

		if(userDetails != null){
			Member member = userDetails.getMember();

			if(store.getBookmarks().size() != 0){
				List<Bookmark> bookmarks = store.getBookmarks();

				for(Bookmark bookmark : bookmarks){
					if(bookmark.getMember().getId() == member.getId()){

						foreignOneStoreResponse.setBookmark(true);
						long totalBookmarks = store.getBookmarks().size();
						foreignOneStoreResponse.setTotalBookmark(totalBookmarks);
						// return foreignOneStoreResponse;

					}

				}
			}

		}

		if(store.getNightPharmacy() == 1){
			foreignOneStoreResponse.setNightBusiness(true);
		}

		if(store.getBookmarks().size() != 0){
			foreignOneStoreResponse.setTotalBookmark(store.getBookmarks().size());
		}

		if(store.getForeignLanguage() != null){
			if(store.getEnglish() == 1){
				english = true;
			}
			if (store.getChinese() == 1){
				chinese = true;
			}
			if(store.getJapanese() == 1){
				japanese = true;
			}

			foreignOneStoreResponse.setLanguage(english, chinese, japanese);
		}
		return foreignOneStoreResponse;
	}
}