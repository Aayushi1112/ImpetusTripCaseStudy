package com.microservice.casestudy.FlightBooking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.casestudy.FlightBooking.entity.FlightBookingInfoAndStatus;
import com.microservice.casestudy.FlightBooking.entity.FlightInfo;
import com.microservice.casestudy.FlightBooking.entity.TourPackageAndStatus;
import com.microservice.casestudy.FlightBooking.service.FlightService;

@RestController
@RequestMapping("/flights")
public class FlightController {
	
	@Autowired
	FlightService flightService;
	
	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome to the flight service";
	}
	
	@GetMapping("/get-flight-info")
	public  List<FlightInfo> getflightInfo() {
		return flightService.getFlightInfo();
	}
	
	@PostMapping("/add-flight-info")
	public FlightInfo addflightInfo(@RequestBody FlightInfo flightInfo) {
		return flightService.addFlightInfo(flightInfo);
	}
	
	@PostMapping("/book-a-flight")
	public FlightBookingInfoAndStatus bookflight(@RequestBody TourPackageAndStatus tourPackage) throws JsonProcessingException {
	int tripBookingId=tourPackage.getTripBookingId();
	int noOfflightSeatsNeeded=tourPackage.getNoOfflightSeats();
	return flightService.bookFlight(tripBookingId, noOfflightSeatsNeeded);
	
	}
}
