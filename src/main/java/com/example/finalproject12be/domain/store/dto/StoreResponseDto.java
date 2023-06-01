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

	//좋아요 기능이 없어서 하드코딩 해 둠
	//TODO: 본인이 누른 좋아요인지 확인하는 기능 추가 후 수정
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
}