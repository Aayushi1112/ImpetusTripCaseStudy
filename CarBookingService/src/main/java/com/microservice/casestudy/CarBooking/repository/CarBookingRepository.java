package com.microservice.casestudy.CarBooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.microservice.casestudy.CarBooking.entity.CarBookingInfoAndStatus;


@Repository
public interface CarBookingRepository extends JpaRepository<CarBookingInfoAndStatus,Integer> {
	CarBookingInfoAndStatus findByTripBookingId(int tripBookingId);
}


