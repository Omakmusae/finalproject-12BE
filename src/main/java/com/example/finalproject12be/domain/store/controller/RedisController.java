package com.example.finalproject12be.domain.store.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.finalproject12be.domain.store.service.RedisService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class RedisController {

	private final RedisService redisService;

	@GetMapping("/test/redis")
	public String test() {
		redisService.redisString();
		return "test";
	}
}
