package com.example.finalproject12be.domain.store.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.Flow;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.finalproject12be.domain.store.OpenApiManager;
import com.example.finalproject12be.domain.store.dto.OneStoreResponseDto;
import com.example.finalproject12be.domain.store.dto.StoreResponseDto;
import com.example.finalproject12be.domain.store.service.StoreService;

import lombok.RequiredArgsConstructor;

@RestController
// @Controller
@RequiredArgsConstructor
// @RequestMapping("api/store")
public class StoreController {

	private final StoreService storeService;

	@GetMapping("/api/store")
	public List<StoreResponseDto> getAllStores(){
		return storeService.getAllStores();
	}

	@GetMapping("/api/store/{id}")
	public OneStoreResponseDto getStore(@PathVariable(name = "id") Long storeId){
		return storeService.getStore(storeId);
	}

	@PostMapping("/api/store/search")
	public List<StoreResponseDto> searchStore(
			@RequestParam("storeName") String storeName,
			@RequestParam("gu") String gu,
			@RequestParam("open") boolean open,
			@RequestParam("holidayBusiness") boolean holidayBusiness,
			@RequestParam("nightBusiness") boolean nightBusiness){
		return storeService.searchStore(storeName, gu, open, holidayBusiness, nightBusiness);
	}



	private final OpenApiManager openApiManager;

	@GetMapping("api/store/open-api")
	public void fetch() {
		openApiManager.fetch();
	}

}