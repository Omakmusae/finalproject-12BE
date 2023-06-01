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
	private String longitude;

	@Column
	private String latitude;

	@OneToOne
	@JoinColumn(name = "BOOKMARK_ID")
	private Bookmark bookmark;

	@OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
	private List<Comment> commentList = new ArrayList<>();

	public Store(String address, String name, String callNumber, String weekdaysTime, String saturdayTime, String sundayTime, String holidayTime, String longitude, String latitude){
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

	public void setBookmark(Bookmark bookmark){
		this.bookmark = bookmark;
	}
}
