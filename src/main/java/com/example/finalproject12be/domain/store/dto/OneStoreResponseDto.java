package com.example.finalproject12be.domain.store.dto;

import java.util.List;

import com.example.finalproject12be.domain.comment.entity.Comment;
import com.example.finalproject12be.domain.store.entity.Store;

import lombok.Getter;

@Getter
public class OneStoreResponseDto {
    private final Long storeId;
    private final String address;
    private final String name;
    private final String callNumber;
    private final String weekdaysTime;
    private final String saturdayTime;
    private final String sundayTime;
    private final String holidayTime;
    private final String longitude;
    private final String latitude;
    private boolean bookmark = false;
    private long totalBookmark = 0;
    private List<Comment> comments = null; //comment 기능 구현 전으로 테스트를 위해 null로 하드코딩
//TODO: comment 추가

        //좋아요 기능이 없어서 하드코딩 해 둠
        //TODO: 본인이 누른 북마크인지 확인하는 기능 추가 후 수정

    public OneStoreResponseDto(Store store) {
        this.storeId = store.getId();
        this.address = store.getAddress();
        this.name = store.getName();
        this.callNumber = store.getCallNumber();
        this.weekdaysTime = store.getWeekdaysTime();
        this.saturdayTime = store.getSaturdayTime();
        this.sundayTime = store.getSundayTime();
        this.holidayTime = store.getHolidayTime();
        this.longitude = store.getLongitude();
        this.latitude = store.getLatitude();
    }
}