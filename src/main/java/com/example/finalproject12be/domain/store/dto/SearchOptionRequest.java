package com.example.finalproject12be.domain.store.dto;

import lombok.Data;

@Data
public class SearchOptionRequest {

	private Integer page;
	private Integer size;
	private String storeName;
	private String gu;
	private boolean open;
	private boolean holidayBusiness;
	private boolean nightBusiness;

	private String radius;
	private String latitude;
	private String longitude;


	public MappedSearchRequest toMappedSearchRequest() {
		return MappedSearchRequest.builder()
			.page(page == null ? null : page.intValue())
			.size(size == null ? null : size.intValue())
			.storeName(storeName == "" ? null : storeName)
			.gu(gu == "" ? null : gu)
			.open(open == true ? true : false)
			.holidayBusiness(holidayBusiness == true ? true : false)
			.nightBusiness(nightBusiness == true ? true : false)
			.baseRadius(radius == "" ? null : radius)
			.baseLatitude(latitude == "" ? null : latitude)
			.baseLongitude(longitude == "" ? null : longitude)
			.build();

	}

}
