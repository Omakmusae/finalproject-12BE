package com.example.finalproject12be.domain.store.dto;

import com.example.finalproject12be.domain.store.entity.Store;

import lombok.Getter;

@Getter
public class ForeignStoreResponse {

	private final Long storeId;
	private final String address;
	private final String name;
	private final String callNumber;
	private final String weekdaysTime;
	private final Double longitude;
	private final Double latitude;
	private boolean bookmark = false;
	private boolean english = false;
	private boolean chinese = false;
	private boolean japanese = false;

	public ForeignStoreResponse(Store store){
		this.storeId = store.getId();
		this.address = store.getAddress();
		this.name = store.getName();
		this.callNumber = store.getCallNumber();
		this.weekdaysTime = store.getWeekdaysTime();
		this.latitude = store.getLatitude();
		this.longitude = store.getLongitude();
	}

	public ForeignStoreResponse(Long storeId, String address, String name, String callNumber, String weekdaysTime,
		Double longitude, Double latitude, Integer english, Integer chinese, Integer japanese) {
		this.storeId = storeId;
		this.address = address;
		this.name = name;
		this.callNumber = callNumber;
		this.weekdaysTime = weekdaysTime;
		this.longitude = longitude;
		this.latitude = latitude;
		this.english = english != null && english == 1;
		this.chinese = chinese != null && chinese == 1;
		this.japanese = japanese != null && japanese == 1;
	}

	public void setBookmark(boolean bookmark){
		this.bookmark = bookmark;
	}

	public void setLanguage(boolean english, boolean chinese, boolean japanese){
		this.english = english;
		this.chinese = chinese;
		this.japanese = japanese;
	}
}
