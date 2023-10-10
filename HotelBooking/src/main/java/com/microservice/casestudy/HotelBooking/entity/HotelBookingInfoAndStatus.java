package com.microservice.casestudy.HotelBooking.entity;

import com.microservice.casestudy.HotelBooking.enums.HotelBookingStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
@Entity

public class HotelBookingInfoAndStatus {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int hotelBookingId;
	private int tripBookingId;
	private int noOfHotelBeds;
	private HotelBookingStatus hotelBookingStatus;
	

}
