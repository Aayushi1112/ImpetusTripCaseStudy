package com.microservice.casestudy.HotelBooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.casestudy.HotelBooking.entity.HotelInfo;


@Repository
public interface HotelRepository extends JpaRepository<HotelInfo,Integer> {
}


