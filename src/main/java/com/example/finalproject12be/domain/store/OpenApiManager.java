// package com.example.finalproject12be.domain.store;
//
// import java.io.BufferedReader;
// import java.io.InputStreamReader;
// import java.io.UnsupportedEncodingException;
// import java.net.HttpURLConnection;
// import java.net.URL;
// import java.net.URLEncoder;
// import java.util.Map;
//
// import org.json.JSONObject;
// import org.json.XML;
// import org.springframework.beans.factory.annotation.Required;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Component;
// import org.springframework.web.client.RestTemplate;
//
// import com.example.finalproject12be.domain.store.entity.Store;
// import com.example.finalproject12be.domain.store.repository.StoreRepository;
//
// import lombok.RequiredArgsConstructor;
//
// @Component
// @RequiredArgsConstructor
// public class OpenApiManager {
// 	private final StoreRepository storeRepository;
//
// 	// private final String BASE_URL = "http://safemap.go.kr/openApiService/data/getPharmacyData.do";
// 	// private final String serviceKey = "?ServiceKey=3CX2XHYE-3CX2-3CX2-3CX2-3CX2XHYEES";
// 	// private final String numOfRows = "&numOfRows=10";
// 	// private final String areaCode = "&pageNo=1";
// 	// private final String contentTypeId = "&dataType=XML";
//
// 	private String makeXml(String num){
// 		try {
// 			StringBuilder strBuilder = new StringBuilder(
// 				"http://safemap.go.kr/openApiService/data/getPharmacyData.do"); /*URL*/
// 			strBuilder.append(
// 				"?" + URLEncoder.encode("serviceKey", "UTF-8") + "=3CX2XHYE-3CX2-3CX2-3CX2-3CX2XHYEES"); /*Service Key*/
// 			strBuilder.append(
// 				"&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(num, "UTF-8")); /*페이지번호*/
// 			strBuilder.append(
// 				"&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10000", "UTF-8")); /*한 페이지 결과 수*/
// 			strBuilder.append(
// 				"&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8")); /*xml(기본값), JSON*/
//
// 			URL url = new URL(strBuilder.toString());
// 			HttpURLConnection con = (HttpURLConnection)url.openConnection();
// 			con.setRequestMethod("GET");
//
// 			int responseCode = con.getResponseCode();
// 			if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM
// 				|| responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
// 				String newUrl = con.getHeaderField("Location");
// 				con = (HttpURLConnection)new URL(newUrl).openConnection();
// 				con.setRequestMethod("GET");
// 			}
//
// 			// Get the response
// 			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
// 			StringBuilder response = new StringBuilder();
// 			String line;
// 			while ((line = reader.readLine()) != null) {
// 				response.append(line);
// 			}
// 			reader.close();
// 			con.disconnect();
//
// 			//xml 문자열로 뽑아내기
// 			String xml = response.toString();
//
// 			return xml;
// 		}catch (Exception e){
// 			System.out.println("error message: " + e.getMessage());
// 			return null;
// 		}
// 	}
//
// 	public void fetch() {
// 		for (int i = 1; i < 4; i++) {
// 			String xml = makeXml(Integer.toString(i));
//
// 			while(xml.contains("<DUTYADDR>")){
//
// 				//xml (String)에서 약국 주소 가져오기
// 				int idx1 = xml.indexOf("<DUTYADDR>");
// 				int idx2 = xml.indexOf("</DUTYADDR>");
// 				// System.out.println("idx1= " + idx1 + "\nidx2= " + idx2);
// 				String address = xml.substring(idx1 + 10, idx2);
// 				// System.out.println("indexOf: " + address);
//
// 				if(address.contains("서울")){
// 					//요소 뽑아내서 entity로 저장하기
// 					//약국 이름 가져오기
// 					idx1 = xml.indexOf("<DUTYNAME>");
// 					idx2 = xml.indexOf("</DUTYNAME>");
// 					System.out.println("idx1= " + idx1 + "\nidx2= " + idx2);
// 					String name = xml.substring(idx1 + 10, idx2);
// 					// System.out.println("indexOf: " + name);
//
// 					//약국 번호 가져오기
// 					idx1 = xml.indexOf("<DUTYTEL1>");
// 					idx2 = xml.indexOf("</DUTYTEL1>");
// 					System.out.println("idx1= " + idx1 + "\nidx2= " + idx2);
// 					String callNumber = xml.substring(idx1 + 10, idx2);
// 					// System.out.println("indexOf: " + callNumber);
//
// 					//약국 시간 가져오기
// 					//평일
// 					//open
// 					idx1 = xml.indexOf("<DUTYTIME1S>");
// 					idx2 = xml.indexOf("</DUTYTIME1S>");
// 					String openHour = xml.substring(idx1 + 12, idx2 - 2);
//
// 					idx1 = xml.indexOf("<DUTYTIME1S>");
// 					idx2 = xml.indexOf("</DUTYTIME1S>");
// 					String openMin = xml.substring(idx1 + 14, idx2);
//
// 					//close
// 					idx1 = xml.indexOf("<DUTYTIME1C>");
// 					idx2 = xml.indexOf("</DUTYTIME1C>");
// 					String closeHour = xml.substring(idx1 + 12, idx2 - 2);
//
// 					idx1 = xml.indexOf("<DUTYTIME1C>");
// 					idx2 = xml.indexOf("</DUTYTIME1C>");
// 					String closeMin = xml.substring(idx1 + 14, idx2);
// 					String weekdaysTime = "평일 " + openHour + ":" + openMin + "~" + closeHour + ":" + closeMin;
// 					// System.out.println("indexOf: " + weekdaysTime);
//
// 					//토요일
// 					//open
// 					idx1 = xml.indexOf("<DUTYTIME6S>");
// 					idx2 = xml.indexOf("</DUTYTIME6S>");
// 					String saturdayTime = xml.substring(idx1 + 12, idx2);
//
// 					if (saturdayTime.equals("null")) {
// 						saturdayTime = null;
// 						// System.out.println("토요일 null");
// 					} else {
// 						openHour = xml.substring(idx1 + 12, idx2 - 2);
//
// 						idx1 = xml.indexOf("<DUTYTIME6S>");
// 						idx2 = xml.indexOf("</DUTYTIME6S>");
// 						openMin = xml.substring(idx1 + 14, idx2);
//
// 						//close
// 						idx1 = xml.indexOf("<DUTYTIME6C>");
// 						idx2 = xml.indexOf("</DUTYTIME6C>");
// 						closeHour = xml.substring(idx1 + 12, idx2 - 2);
//
// 						idx1 = xml.indexOf("<DUTYTIME6C>");
// 						idx2 = xml.indexOf("</DUTYTIME6C>");
// 						closeMin = xml.substring(idx1 + 14, idx2);
// 						saturdayTime = "토 " + openHour + ":" + openMin + "~" + closeHour + ":" + closeMin;
// 						// System.out.println("indexOf: " + saturdayTime);
// 					}
//
// 					//일요일
// 					//open
// 					idx1 = xml.indexOf("<DUTYTIME7S>");
// 					idx2 = xml.indexOf("</DUTYTIME7S>");
// 					String sundayTime = xml.substring(idx1 + 12, idx2);
//
// 					if (sundayTime.equals("null")) {
// 						sundayTime = null;
// 						// System.out.println("일요일 null");
// 					} else {
// 						openHour = xml.substring(idx1 + 12, idx2 - 2);
//
// 						idx1 = xml.indexOf("<DUTYTIME7S>");
// 						idx2 = xml.indexOf("</DUTYTIME7S>");
// 						openMin = xml.substring(idx1 + 14, idx2);
//
// 						//close
// 						idx1 = xml.indexOf("<DUTYTIME7C>");
// 						idx2 = xml.indexOf("</DUTYTIME7C>");
// 						closeHour = xml.substring(idx1 + 12, idx2 - 2);
//
// 						idx1 = xml.indexOf("<DUTYTIME7C>");
// 						idx2 = xml.indexOf("</DUTYTIME7C>");
// 						closeMin = xml.substring(idx1 + 14, idx2);
// 						sundayTime = "일 " + openHour + ":" + openMin + "~" + closeHour + ":" + closeMin;
// 						// System.out.println("indexOf: " + sundayTime);
// 					}
//
// 					//공휴일
// 					//open
// 					idx1 = xml.indexOf("<DUTYTIME8S>");
// 					idx2 = xml.indexOf("</DUTYTIME8S>");
// 					String holidayTime = xml.substring(idx1 + 12, idx2);
//
// 					if (holidayTime.equals("null")) {
// 						holidayTime = null;
// 						// System.out.println("공휴일 null");
// 					} else {
// 						openHour = xml.substring(idx1 + 12, idx2 - 2);
//
// 						idx1 = xml.indexOf("<DUTYTIME8S>");
// 						idx2 = xml.indexOf("</DUTYTIME8S>");
// 						openMin = xml.substring(idx1 + 14, idx2);
//
// 						//close
// 						idx1 = xml.indexOf("<DUTYTIME8C>");
// 						idx2 = xml.indexOf("</DUTYTIME8C>");
// 						closeHour = xml.substring(idx1 + 12, idx2 - 2);
//
// 						idx1 = xml.indexOf("<DUTYTIME8C>");
// 						idx2 = xml.indexOf("</DUTYTIME8C>");
// 						closeMin = xml.substring(idx1 + 14, idx2);
// 						holidayTime = "공휴일 " + openHour + ":" + openMin + "~" + closeHour + ":" + closeMin;
// 						// System.out.println("indexOf: " + holidayTime);
// 					}
//
// 					//경도
// 					idx1 = xml.indexOf("<LON>");
// 					idx2 = xml.indexOf("</LON>");
// 					String longitude = xml.substring(idx1 + 5, idx2);
// 					// System.out.println("indexOf: " + longitude);
//
// 					//위도
// 					idx1 = xml.indexOf("<LAT>");
// 					idx2 = xml.indexOf("</LAT>");
// 					String latitude = xml.substring(idx1 + 5, idx2);
// 					// System.out.println("indexOf: " + latitude);
//
// 					Store store = new Store(address, name, callNumber, weekdaysTime, saturdayTime, sundayTime, holidayTime, longitude,
// 						latitude);
//
// 					storeRepository.save(store);
//
//
// 				}
//
// 				// 가장 앞에 있는 데이터 지우기
// 				idx1 = xml.indexOf("</item>");
// 				// idx2 = xml.indexOf("<item>");
// 				if(idx1 == -1){
// 					break;
// 				}
// 				xml = xml.substring(idx1 + 6);
// 				// System.out.println(xml);
//
// 			}
//
// 		}
//
// 	}
// }