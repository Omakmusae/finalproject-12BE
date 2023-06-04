// package com.example.finalproject12be;
//
// import com.example.finalproject12be.domain.store.dto.myLocationDto;
// import com.example.finalproject12be.domain.store.dto.radiusDto;
// import com.example.finalproject12be.domain.store.service.GeometryUtil;
//
// public class test {
// 	public static void main(String[] args) {
// 		Double baseLatitude = 37.5192663683;
// 		Double baseLongitude = 127.0496002823;
// 		Double distance = 1.0; // 반경 1km
//
// 		radiusDto result = GeometryUtil.calculateAllDirections(baseLatitude, baseLongitude, distance);
//
// 		System.out.println("북쪽 위도: " + result.getNorthLatitude() + ", 경도: " + result.getNorthLongitude());
// 		System.out.println("동쪽 위도: " + result.getEastLatitude() + ", 경도: " + result.getEastLongitude());
// 		System.out.println("남쪽 위도: " + result.getSouthLatitude() + ", 경도: " + result.getSouthLongitude());
// 		System.out.println("서쪽 위도: " + result.getWestLatitude() + ", 경도: " + result.getWestLongitude());
//
// 	}
// }
