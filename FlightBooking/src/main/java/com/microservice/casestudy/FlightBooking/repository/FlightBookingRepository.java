package com.microservice.casestudy.FlightBooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.microservice.casestudy.FlightBooking.entity.FlightBookingInfoAndStatus;


@Repository
public interface FlightBookingRepository extends JpaRepository<FlightBookingInfoAndStatus,Integer> {
	FlightBookingInfoAndStatus findByTripBookingId(int tripBookingId);
}


