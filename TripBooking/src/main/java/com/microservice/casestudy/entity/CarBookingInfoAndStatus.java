package com.microservice.casestudy.entity;

import com.microservice.casestudy.enums.CarBookingStatus;

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

public class CarBookingInfoAndStatus {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int carBookingId;
	private int tripBookingId;
	private int noOfSeats;
	private CarBookingStatus carBookingStatus;
	private String serviceName="CAR";
	

}
