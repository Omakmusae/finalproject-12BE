package com.example.finalproject12be.domain.store.dto;

import lombok.Data;


@Data
public class SearchForeignOptionRequest {
	private Integer page;
	private Integer size;
	private String storeName;
	private String gu;
	private boolean open;
	private boolean holidayBusiness;
	private boolean nightBusiness;

	private boolean english;
	private boolean chinese;
	private boolean japanese;

	private String radius;
	private String latitude;
	private String longitude;

	public MappedSearchForeignRequest toMappedSearchRequest() {
		return MappedSearchForeignRequest.builder()
			.page(page == null ? null : page.intValue())
			.size(size == null ? null : size.intValue())
			.storeName(storeName == "" ? null : storeName)
			.gu(gu == "" ? null : gu)
			.open(open == true ? true : false)
			.holidayBusiness(holidayBusiness == true ? true : false)
			.nightBusiness(nightBusiness == true ? true : false)

			.english(english == true? 1 : 0)
			.chinese(chinese  == true? 1 : 0)
			.japanese(japanese == true? 1 : 0)

			.baseRadius(radius == "" ? null : radius)
			.baseLatitude(latitude == "" ? null : latitude)
			.baseLongitude(longitude == "" ? null : longitude)
			.build();

	}
}
