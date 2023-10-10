package com.microservice.casestudy.HotelBooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.casestudy.HotelBooking.entity.HotelBookingInfoAndStatus;


@Repository
public interface HotelBookingRepository extends JpaRepository<HotelBookingInfoAndStatus,Integer> {
	
	HotelBookingInfoAndStatus findByTripBookingId(int tripBookingId);
}


