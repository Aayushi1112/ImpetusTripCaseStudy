package com.microservice.casestudy.HotelBooking.entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TourPackageAndStatus {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int tripBookingId;
	private String tripFrom;
	private String tripTo;
	private int noOfhotelBeds;
	private int noOfcarSeats;
	private int noOfflightSeats;
	private String tripbookingStatus;
	private String HotelStatus;
	private String CarStatus;
	private String FlightStatus;


	
}


