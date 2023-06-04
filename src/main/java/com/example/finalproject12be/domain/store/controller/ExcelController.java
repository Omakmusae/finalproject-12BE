package com.example.finalproject12be.domain.store.controller;

import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ClassPathResource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.example.finalproject12be.domain.store.entity.Store;
import com.example.finalproject12be.domain.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ExcelController {

	private final StoreRepository storeRepository;

	@GetMapping("/read-excel")
	@ResponseBody
	public void readExcel() {//public List<List<Object>> readExcel() {
		List<List<Object>> data = new ArrayList<>();

		try {
			// 엑셀 파일 클래스패스 리소스 가져오기
			ClassPathResource resource = new ClassPathResource("foreign.xlsx");
			InputStream file = resource.getInputStream();

			// 엑셀 파일 열기
			Workbook workbook = WorkbookFactory.create(file);

			// 첫 번째 시트 선택
			Sheet sheet = workbook.getSheetAt(0);

			// 모든 행 반복
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				List<Object> rowData = new ArrayList<>();

				// 모든 셀 반복
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					Object cellValue;

					// 셀 값 읽기
					switch (cell.getCellType()) {
						case STRING:
							cellValue = cell.getStringCellValue();
							break;
						case NUMERIC:
							cellValue = cell.getNumericCellValue();
							break;
						// 필요한 경우 다른 셀 유형도 처리할 수 있습니다.
						default:
							cellValue = null;
					}

					rowData.add(cellValue);
				}

				data.add(rowData);
			}

			// 파일 및 워크북 닫기
			file.close();
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//요소
		//ing
		for(int i = 4; i < 420; i++){
			List<Object> object = data.get(i);

			String callNumber = String.valueOf(object.get(4));
			Optional<Store> storeOptional = storeRepository.findByCallNumber(callNumber);

			if(storeOptional.isPresent()){

				Store store = storeOptional.get();
				int foreignLanguage = 1;
				int english = 0;
				int chinese = 0;
				int japanese = 0;

				//english
				if(object.get(5) != null){
					english = 1;
				}

				//chinese
				if(object.get(6) != null){
					chinese = 1;
				}

				//japanese
				if(object.get(7) != null){
					japanese = 1;
				}

				store.setForeign(foreignLanguage, english, chinese, japanese);
				storeRepository.save(store);

			}

		}

		// return data;
	}
}