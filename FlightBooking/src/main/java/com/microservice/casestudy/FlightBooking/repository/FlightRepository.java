package com.microservice.casestudy.FlightBooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.casestudy.FlightBooking.entity.FlightInfo;


@Repository
public interface FlightRepository extends JpaRepository<FlightInfo,Integer> {
}


