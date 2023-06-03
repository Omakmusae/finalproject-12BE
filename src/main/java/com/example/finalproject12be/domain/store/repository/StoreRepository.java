package com.example.finalproject12be.domain.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.finalproject12be.domain.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findAllByAddressContaining(String gu);

    List<Store> findAllByHolidayTimeIsNotNull();

    List<Store> findAllByWeekdaysTimeContaining(String time);

    List<Store> findAllBysaturdayTimeContaining(String time);

    List<Store> findAllBysundayTimeContaining(String time);

    List<Store> findAllByNameContaining(String storeName);

    Optional<Store> findByCallNumber(Object callNumber);
}