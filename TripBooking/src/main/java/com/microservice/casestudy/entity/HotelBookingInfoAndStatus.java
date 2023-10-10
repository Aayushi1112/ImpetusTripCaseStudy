package com.microservice.casestudy.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microservice.casestudy.enums.HotelBookingStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HotelBookingInfoAndStatus {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int hotelBookingId;
	private int tripBookingId;
	private int noOfHotelBeds;
	private HotelBookingStatus hotelBookingStatus;
	private String serviceName="HOTEL";
	
//	@JsonCreator
//	public  HotelBookingInfoAndStatus
//	(@JsonProperty("hotelBookingId")int hotelBookingId,
//			@JsonProperty("tripBookingId")int tripBookingId,
//			@JsonProperty("noOfHotelBeds") int noOfHotelBeds,
//	@JsonProperty("hotelBookingStatus") HotelBookingStatus hotelBookingStatus)
//	
//	{this.hotelBookingId=hotelBookingId;
//	this.hotelBookingStatus=hotelBookingStatus;
//	this.noOfHotelBeds=noOfHotelBeds;
//	this.tripBookingId=tripBookingId;
//		
//	}

}
