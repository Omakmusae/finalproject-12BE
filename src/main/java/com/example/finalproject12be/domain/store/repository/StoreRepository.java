package com.example.finalproject12be.domain.store.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.finalproject12be.domain.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findAllByName(String name);

    List<Store> findAllByAddressContaining(String gu);

    List<Store> findAllByHolidayTimeIsNotNull();

    List<Store> findAllByWeekdaysTimeContaining(String time);

    List<Store> findAllBysaturdayTimeContaining(String time);

    List<Store> findAllBysundayTimeContaining(String time);

    // @Query("SELECT s FROM Store s WHERE address LIKE '%{gu}%'")
    // List<Store> findAllByGu(String gu);
}