package com.microservice.casestudy.HotelBooking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class HotelInfo {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int hotelId;
	private String hotelName;
	private String hotelperBedPrice;
	private int noOfBedsAvailableInHotel;
	}
