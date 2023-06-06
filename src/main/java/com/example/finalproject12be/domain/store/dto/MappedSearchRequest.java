package com.example.finalproject12be.domain.store.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MappedSearchRequest {

	private Double baseRadius;
	private Double baseLatitude;
	private Double baseLongitude;
	private String address;

	@Builder
	public MappedSearchRequest(Double baseRadius, Double baseLatitude, Double baseLongitude,
		String address) {
		this.baseRadius= baseRadius;
		this.baseLatitude = baseLatitude;
		this.baseLongitude = baseLongitude;
		this.address = address;
	}
}
