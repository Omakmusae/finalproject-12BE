package com.example.finalproject12be.domain.store.entity;

import javax.persistence.*;

import com.example.finalproject12be.domain.bookmark.entity.Bookmark;

import com.example.finalproject12be.domain.comment.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Store {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "STORE_ID")
	private Long id;

	@Column
	private String address;

	@Column
	private String name;

	@Column
	private String callNumber;

	//평일 운영 시간
	@Column
	private String weekdaysTime;

	@Column
	private String saturdayTime;

	@Column
	private String sundayTime;

	@Column
	private String holidayTime;

	@Column
	private Double longitude;

	@Column
	private Double latitude;

	@OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
	private List<Comment> commentList = new ArrayList<>();

	@OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
	private List<Bookmark> bookmarks;

	public Store(String address, String name, String callNumber, String weekdaysTime, String saturdayTime, String sundayTime, String holidayTime, Double longitude, Double latitude){
		this.address = address;
		this.name = name;
		this.callNumber = callNumber;
		this.weekdaysTime = weekdaysTime;
		this.saturdayTime = saturdayTime;
		this.sundayTime = sundayTime;
		this.holidayTime = holidayTime;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public void deleteBookmark(Bookmark bookmark){
		this.bookmarks.remove(bookmark);
	}

	public void addBookmark(Bookmark bookmark){
		this.bookmarks.add(bookmark);
	}
}
