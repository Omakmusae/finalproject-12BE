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
	private String saturdayTime = null;
	private String sundayTime = null;
	private String holidayTime = null;
	private final Double longitude;
	private final Double latitude;
	private boolean nightBusiness = false;
	private boolean bookmark = false;
	private long totalBookmark = 0;
	private boolean english = false;
	private boolean chinese = false;
	private boolean japanese = false;
	private int totalCommentsNum = 0;

	public ForeignOneStoreResponse(Store store){
		this.storeId = store.getId();
		this.address = store.getAddress();
		this.name = store.getName();
		this.callNumber = store.getCallNumber();
		this.weekdaysTime = store.getWeekdaysTime();
		this.longitude = store.getLongitude();
		this.latitude = store.getLatitude();
		if(store.getCommentList() != null){
			this.totalCommentsNum = store.getCommentList().size();
		}
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

	public void setNightBusiness(boolean nightBusiness){
		this.nightBusiness = nightBusiness;
	}

	public void setSaturdayTime(String saturdayTime){
		this.saturdayTime = saturdayTime.substring(2, 15);
	}

	public void setSundayTime(String sundayTime){
		this.sundayTime = sundayTime.substring(2, 15);
	}

	public void setHolidayTime(String holidayTime){
		this.holidayTime = holidayTime.substring(4, 17);
	}
}
