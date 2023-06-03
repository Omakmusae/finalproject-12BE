package com.example.finalproject12be.domain.store.dto;

import com.example.finalproject12be.domain.store.entity.Store;

import lombok.Getter;

@Getter
public class StoreResponseDto {

	private Long storeId;

	private String address;

	private String name;

	private String callNumber;

	private String weekdaysTime;

	private String longitude;

	private String latitude;

	private boolean bookmark = false;

	public StoreResponseDto(Store store) {
		this.storeId = store.getId();
		this.address = store.getAddress();
		this.name = store.getName();
		this.callNumber = store.getCallNumber();
		this.weekdaysTime = store.getWeekdaysTime();
		this.longitude = store.getLongitude();
		this.latitude = store.getLatitude();
	}

	public void setBookmark(boolean bookmark){
		this.bookmark = bookmark;
	}
}