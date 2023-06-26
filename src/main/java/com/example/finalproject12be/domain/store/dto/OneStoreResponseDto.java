package com.example.finalproject12be.domain.store.dto;

import com.example.finalproject12be.domain.store.entity.Store;

import lombok.Getter;

@Getter
public class OneStoreResponseDto {
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
    private boolean bookmark = false;
    private long totalBookmark = 0;
    private boolean nightBusiness = false;
    private int totalCommentsNum = 0;

    public OneStoreResponseDto(Store store) {
        this.storeId = store.getId();
        this.address = store.getAddress();
        this.name = store.getName();
        this.callNumber = store.getCallNumber();
        this.weekdaysTime = store.getWeekdaysTime().substring(3);
        this.longitude = store.getLongitude();
        this.latitude = store.getLatitude();
        if(store.getCommentList() != null){
            this.totalCommentsNum = store.getCommentList().size();
        }
    }

    public void setBookmark(boolean bookmark){
        this.bookmark = bookmark;
    }

    public void setTotalBookmark(long totalBookmark){
        this.totalBookmark = totalBookmark;
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