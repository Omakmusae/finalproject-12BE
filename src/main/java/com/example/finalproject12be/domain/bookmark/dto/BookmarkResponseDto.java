package com.example.finalproject12be.domain.bookmark.dto;

import com.example.finalproject12be.domain.store.entity.Store;

import lombok.Getter;

@Getter
public class BookmarkResponseDto {

	private Long storeId;
	private String address;
	private String name;
	private String callNumber;
	private String weekdaysTime;
	private boolean bookmark = true; //이걸 줄 필요가 있나 ?
	private long totalBookmark;
	private boolean holidayBusiness = false;
	private boolean nightBusiness = false;

	public BookmarkResponseDto(Store store) {
		this.storeId = store.getId();
		this.address = store.getAddress();
		this.name = store.getName();
		this.callNumber = store.getCallNumber();
		this.weekdaysTime = store.getWeekdaysTime();
		this.totalBookmark = store.getBookmarks().size();
	}

	public void setHolidayBusiness(boolean holidayBusiness){
		this.holidayBusiness = holidayBusiness;
	}

	public void setNightBusiness(boolean nightBusiness){
		this.nightBusiness = nightBusiness;
	}
}
