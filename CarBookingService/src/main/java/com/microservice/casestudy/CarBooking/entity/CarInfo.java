package com.microservice.casestudy.CarBooking.entity;

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

public class CarInfo {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int carId;
	private String carName;
	private String perSeatprice;
	private int noOfSeatsAvailableInCar;
	

}
