package com.microservice.casestudy.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.retry.annotation.CircuitBreaker;
//import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.casestudy.entity.CarInfo;
import com.microservice.casestudy.entity.FlightInfo;
import com.microservice.casestudy.entity.HotelInfo;
import com.microservice.casestudy.entity.TourPackageAndStatus;
import com.microservice.casestudy.entity.TripCatalog;
import com.microservice.casestudy.servcie.TripBookingService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/trips")
public class TripBookingController {

	@Autowired
	private TripBookingService service;

	Logger logger = LoggerFactory.getLogger(TripBookingController.class);

	@GetMapping
	public String greeting() {
		logger.info("Greeting from trips");
		return "Welcome to trip booking service.We give you the best service in Trip booking";

	}
	
	@GetMapping("/check-trip-status/{tripBookingId}")
	public TourPackageAndStatus getTripStatus(@PathVariable int tripBookingId)
	
	{
		return service.getTripStatus(tripBookingId);
	}
	
	@GetMapping("/trip-hotel-service")
	@CircuitBreaker(name="userHotelBreaker",fallbackMethod="hotelFallback")
    public List<HotelInfo> getTripHotel() {
        List<HotelInfo> hotelInfo = service.getTripHotel();
		return hotelInfo;
	}
	
	public List<HotelInfo> hotelFallback(Exception ex){
		logger.info("Fallback is executed because  hotel service is down:",ex.getMessage());
		HotelInfo h=new HotelInfo();
		h.setHotelName("Sorry Currently we are not able to fetch any hotel information");
		h.setNoOfBedsAvailableInHotel(0);
		List<HotelInfo> hotelList=new ArrayList<>();
		hotelList.add(h);
		return hotelList;
	}
	
	@GetMapping("/trip-car-service")
	@CircuitBreaker(name="CarBreaker",fallbackMethod="carFallback")

	public List<CarInfo> getTripCar() {

		List<CarInfo> carInfo = service.getTripCar();
		return carInfo;
	}
	@GetMapping("/trip-flight-service")
	@CircuitBreaker(name="FlightBreaker",fallbackMethod="flightFallback")

	public List<FlightInfo> getTripFlight() {

		List<FlightInfo> flightInfo = service.getTripFlight();
		return flightInfo;
	}

	@GetMapping("/trip-catalog")
	public List<TripCatalog> getTripCatalog() {
		logger.info("Getting the trip catalog");
		return service.getTripCatalog();

	}

	@PostMapping("/add-trip")
	public String addTripCatalog(@RequestBody TripCatalog tripCatalog) {
		return service.addTripCatalog(tripCatalog);
	}

	@PostMapping("/book-trip")
	public TourPackageAndStatus bookTrip(@RequestBody TourPackageAndStatus tripInclusion)
			throws JsonProcessingException {
		logger.info("A trip creation ticket is issued.The details are as follows : {} ", tripInclusion);
		return service.bookTrip(tripInclusion);
	}
	
	

	public ResponseEntity<List<CarInfo>> carFallback(Exception ex){
		logger.info("Fallback is executed because car service is down:",ex.getMessage());
		List<CarInfo> carList=new ArrayList<>();
		carList.add(new CarInfo("sorry car not responding"));
		return new ResponseEntity<>(carList,HttpStatus.OK);
	}
	public ResponseEntity<List<FlightInfo>> flightFallback(Exception ex){
		logger.info("Fallback is executed because flight service is down:",ex.getMessage());
		List<FlightInfo> flightList=new ArrayList<>();
		flightList.add(new FlightInfo("sorry flight not responding"));
		return new ResponseEntity<>(flightList,HttpStatus.OK);
	}

	

}
