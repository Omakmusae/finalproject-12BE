package com.example.finalproject12be.domain.store.dto;

import com.example.finalproject12be.domain.store.entity.Store;

import lombok.Getter;

@Getter
public class ForeignOneStoreResponse {
	private final Long storeId;
	private final String address;
	private final String name;
	private final String callNumber;
	private final String weekdaysTime;
	private final String saturdayTime;
	private final String sundayTime;
	private final String holidayTime;
	private final Double longitude;
	private final Double latitude;
	private boolean bookmark = false;
	private long totalBookmark = 0;
	private boolean english = false;
	private boolean chinese = false;
	private boolean japanese = false;

	public ForeignOneStoreResponse(Store store){
		this.storeId = store.getId();
		this.address = store.getAddress();
		this.name = store.getName();
		this.callNumber = store.getCallNumber();
		this.weekdaysTime = store.getWeekdaysTime();
		this.saturdayTime = store.getSaturdayTime();
		this.sundayTime = store.getSundayTime();
		this.holidayTime = store.getHolidayTime();
		this.longitude = store.getLongitude();
		this.latitude = store.getLatitude();
		// this.totalBookmark = store.getBookmarks().size();
		// this.english = store.getEnglish();
		// this.chinese = store.getChinese();
		// this.japanese = store.getJapanese();
	}

	public void setBookmark(boolean bookmark){
		this.bookmark = bookmark;
	}

	public void setTotalBookmark(long totalBookmark){
		this.totalBookmark = totalBookmark;
	}

	public void setLanguage(boolean english, boolean chinese, boolean japanese){
		this.english = english;
		this.chinese = chinese;
		this.japanese = japanese;
	}
}
