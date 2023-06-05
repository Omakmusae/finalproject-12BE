// package com.example.finalproject12be.domain.store.service;
//
// import com.example.finalproject12be.domain.store.dto.myLocationDto;
// import com.example.finalproject12be.domain.store.dto.radiusDto;
//
// public class GeometryUtil {
//
// 	public static myLocationDto calculate(Double baseLatitude, Double baseLongitude, Double distance,
// 		Double bearing) {
// 		// 위도와 경도를 라디안 단위로 변환합니다.
// 		Double radianLatitude = toRadian(baseLatitude);
// 		Double radianLongitude = toRadian(baseLongitude);
//
// 		// 방위각을 라디안 단위로 변환
// 		Double radianAngle = toRadian(bearing);
// 		// 거리를 반지름으로 나누어 라디안 거리로 변환
// 		Double distanceRadius = distance / 6371.01; // 6371은 지구의 평균 반지름
//
// 		// 실제 위치 계산을 위해 수학적인 계산을 수행
// 		Double latitude = Math.asin(sin(radianLatitude) * cos(distanceRadius) +
// 			cos(radianLatitude) * sin(distanceRadius) * cos(radianAngle));
// 		Double longitude = radianLongitude + Math.atan2(sin(radianAngle) * sin(distanceRadius) *
// 			cos(radianLatitude), cos(distanceRadius) - sin(radianLatitude) * sin(latitude));
//
// 		// 경도를 정규화
// 		longitude = normalizeLongitude(longitude);
// 		// 계산된 라디안 값을 도 단위로 변환하여 Location 객체를 생성하여 반환
// 		return new myLocationDto(toDegree(latitude), toDegree(longitude));
// 	}
//
// 	public static radiusDto calculateAllDirections(Double baseLatitude, Double baseLongitude, Double distance) {
// 		myLocationDto north = calculate(baseLatitude, baseLongitude, distance, 0.0);
// 		myLocationDto east = calculate(baseLatitude, baseLongitude, distance, 90.0);
// 		myLocationDto south = calculate(baseLatitude, baseLongitude, distance, 180.0);
// 		myLocationDto west = calculate(baseLatitude, baseLongitude, distance, 270.0);
//
// 		return new radiusDto(
// 			north.getLatitude(), north.getLongitude(),
// 			east.getLatitude(), east.getLongitude(),
// 			south.getLatitude(), south.getLongitude(),
// 			west.getLatitude(), west.getLongitude());
// 	}
//
// 	private static Double toRadian(Double coordinate) {
// 		return coordinate * Math.PI / 180.0;
// 	}
//
// 	private static Double toDegree(Double coordinate) {
// 		return coordinate * 180.0 / Math.PI;
// 	}
//
// 	private static Double sin(Double coordinate) {
// 		return Math.sin(coordinate);
// 	}
//
// 	private static Double cos(Double coordinate) {
// 		return Math.cos(coordinate);
// 	}
//
// 	private static Double normalizeLongitude(Double longitude) { // 경도(longitude) 값을 정규화
// 		return (longitude + 540) % 360 - 180;
// 	}
// }