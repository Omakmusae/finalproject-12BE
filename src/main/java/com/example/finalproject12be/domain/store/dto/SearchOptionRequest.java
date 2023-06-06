// package com.example.finalproject12be.domain.store.dto;
//
// import lombok.Data;
//
// @Data
// public class SearchOptionRequest {
//
// 	private Double baseRadius;
// 	private Double baseLatitude;
// 	private Double baseLongitude;
// 	private String address;
//
// 	public MappedSearchRequest toMappedSearchRequest() {
// 		return MappedSearchRequest.builder()
// 			.baseRadius(baseRadius == null ? null : Double.valueOf(String.valueOf(baseRadius)))
// 			.baseLatitude(baseLatitude == null ? null : Double.valueOf(String.valueOf(baseLatitude)))
// 			.baseLongitude(baseLongitude == null ? null : Double.valueOf(String.valueOf(baseLongitude)))
// 			.address(address == null ? null : String.valueOf(address))
// 			.build();
//
// 	}
//
// }
