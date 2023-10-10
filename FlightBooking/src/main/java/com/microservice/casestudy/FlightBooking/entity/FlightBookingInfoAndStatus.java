package com.microservice.casestudy.FlightBooking.entity;



import com.microservice.casestudy.FlightBooking.enums.FlightBookingStatus;

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

public class FlightBookingInfoAndStatus {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int flightBookingId;
	private int tripBookingId;
	private int noOfFlightSeats;
	private FlightBookingStatus flightBookingStatus;
	

}
