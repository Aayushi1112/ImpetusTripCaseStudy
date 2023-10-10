package com.microservice.casestudy.CarBooking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.casestudy.CarBooking.entity.CarBookingInfoAndStatus;
import com.microservice.casestudy.CarBooking.entity.CarInfo;
import com.microservice.casestudy.CarBooking.entity.TourPackageAndStatus;
import com.microservice.casestudy.CarBooking.service.CarService;

@RestController
@RequestMapping("/cars")
public class CarController {
	
	@Autowired
	CarService carService;
	
	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome to the car service";
	}
	
	@GetMapping("/get-car-info")
	public  List<CarInfo> getcarInfo() {
		return carService.getCarInfo();
	}
	
	@PostMapping("/add-car-info")
	public CarInfo addcarInfo(@RequestBody CarInfo carInfo) {
		return carService.addCarInfo(carInfo);
	}
	
	@PostMapping("/book-a-car")
	public CarBookingInfoAndStatus bookcar(@RequestBody TourPackageAndStatus tourPackage) throws JsonProcessingException {
	int tripBookingId=tourPackage.getTripBookingId();
	int noOfcarSeatsNeeded=tourPackage.getNoOfcarSeats();
	return carService.bookCar(tripBookingId, noOfcarSeatsNeeded);
	
	}
}
