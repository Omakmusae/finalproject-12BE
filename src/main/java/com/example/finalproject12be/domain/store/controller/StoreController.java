package com.example.finalproject12be.domain.store.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.Flow;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.example.finalproject12be.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
// @Controller
@RequiredArgsConstructor
// @RequestMapping("api/store")
public class StoreController {

	private final StoreService storeService;

	@GetMapping("/api/store")
	public List<StoreResponseDto> getAllStores(@AuthenticationPrincipal UserDetailsImpl userDetails){
		return storeService.getAllStores(userDetails);
	}

	@GetMapping("/api/store/{id}")
	public OneStoreResponseDto getStore(
		@PathVariable(name = "id") Long storeId,
		@AuthenticationPrincipal UserDetailsImpl userDetails){
		return storeService.getStore(storeId, userDetails);
	}

	@GetMapping("/api/store/search")
	public List<StoreResponseDto> searchStore(
		@RequestParam("storeName") String storeName,
		@RequestParam("gu") String gu,
		@RequestParam("open") boolean open,
		@RequestParam("holidayBusiness") boolean holidayBusiness,
		@RequestParam("nightBusiness") boolean nightBusiness,
		@AuthenticationPrincipal UserDetailsImpl userDetails){
		return storeService.searchStore(storeName, gu, open, holidayBusiness, nightBusiness, userDetails);
	}



	private final OpenApiManager openApiManager;

	@GetMapping("api/store/open-api")
	public void fetch() {
		openApiManager.fetch();
	}

}