package com.example.finalproject12be.domain.store.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MappedSearchForeignRequest {

	private int page;
	private int size;
	private String storeName;
	private String gu;
	private boolean open;
	private boolean holidayBusiness;
	private boolean nightBusiness;

	private Integer english;
	private Integer chinese;
	private Integer japanese;

	private String radius;
	private String latitude;
	private String longitude;


	@Builder
	public MappedSearchForeignRequest(int page, int size, String storeName, String gu, boolean open,
		boolean holidayBusiness, boolean nightBusiness, Integer english, Integer chinese, Integer japanese,
		String baseRadius, String baseLatitude, String baseLongitude)
	{
		this.page = page;
		this.size = size;
		this.storeName = storeName;
		this.gu = gu;
		this.open = open;
		this.holidayBusiness = holidayBusiness;
		this.nightBusiness = nightBusiness;
		this.english = english;
		this.chinese = chinese;
		this.japanese = japanese;
		this.radius= baseRadius;
		this.latitude = baseLatitude;
		this.longitude = baseLongitude;
	}
}
