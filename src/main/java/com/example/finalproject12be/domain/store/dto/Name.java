package com.example.finalproject12be.domain.store.dto;


import com.example.finalproject12be.domain.store.entity.Store;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Name {

	private String name;

	public Name(Store store) {
		this.name = store.getName();
	}

	public Name(String name) {
		this.name = name;
	}

}
