package com.microservice.casestudy.CarBooking.service;

import java.util.List;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.casestudy.CarBooking.entity.CarBookingInfoAndStatus;
import com.microservice.casestudy.CarBooking.entity.CarInfo;
import com.microservice.casestudy.CarBooking.enums.CarBookingStatus;
import com.microservice.casestudy.CarBooking.event.CarBookingStatusEventPublisher;
import com.microservice.casestudy.CarBooking.repository.CarBookingRepository;
import com.microservice.casestudy.CarBooking.repository.CarRepository;
/**
 * *
 *1.here from the car repo we have to find if required carseats are available or not
2.if yes then deduct no of seats in car and update the car repo and also
3.update the booking repository with the corresponding tripbookingid
4.else cancel do not book
 */
 
@Service
public class CarService {
	@Autowired
	CarBookingStatusEventPublisher carEventPublisher;
	@Autowired
	CarRepository carRepository;
	@Autowired
	CarBookingRepository carBookingRepo;
	ObjectMapper om = new ObjectMapper();
	Logger logger = LoggerFactory.getLogger(CarService.class);


	private int CarId = 1;

	public CarBookingInfoAndStatus bookCar(int tripBookingId, int noOfCarSeatsNeeded) throws JsonProcessingException {
		logger.info("In car service and booking the car for tripBookingId:{}", tripBookingId);

		Optional<CarInfo> carinfo=carRepository.findById(CarId);
		int noOfSeatsVacant=carinfo.get().getNoOfSeatsAvailableInCar();
		
		if(noOfCarSeatsNeeded<noOfSeatsVacant)
		{
			logger.info("Wonderful!!!We have the car seats needed by you.Booking {} seats", noOfCarSeatsNeeded);

	       carinfo.get().setNoOfSeatsAvailableInCar(noOfSeatsVacant-noOfCarSeatsNeeded);
	       carRepository.save(carinfo.get());
	       
	       CarBookingInfoAndStatus carBookedInfo=new CarBookingInfoAndStatus();
	       carBookedInfo.setTripBookingId(tripBookingId);
	       carBookedInfo.setCarBookingStatus(CarBookingStatus.CAR_BOOKED);
	       carBookedInfo.setNoOfSeats(noOfCarSeatsNeeded);
	       CarBookingInfoAndStatus bookedCarInfoAndStatus=carBookingRepo.save(carBookedInfo);
	       logger.info("Saved the car booked details in the car db:{}", bookedCarInfoAndStatus);
			String createdCarTripStr = om.writeValueAsString(bookedCarInfoAndStatus);
			carEventPublisher.publish(createdCarTripStr);
			logger.info("Putting a car_BOOKED message in the car event  with info as:\n{}",createdCarTripStr);
			
	       return bookedCarInfoAndStatus;
	       }
		else
		{
			logger.info("Sorry!!!We dont have the car seats needed by you.");

           CarBookingInfoAndStatus bookedCarInfoAndStatus=new CarBookingInfoAndStatus();
           bookedCarInfoAndStatus.setTripBookingId(tripBookingId);
           bookedCarInfoAndStatus.setCarBookingStatus(CarBookingStatus.CAR_BOOKING_CANCELLED);
           bookedCarInfoAndStatus.setNoOfSeats(0);
           String createdCarTripStr = om.writeValueAsString(bookedCarInfoAndStatus);
			
			carEventPublisher.publish(createdCarTripStr);
			logger.info("Putting a CAR_BOOKING_CANCELLED message in the car event  with info as:\n {}",createdCarTripStr);
       
		       return bookedCarInfoAndStatus;
		}	
	}
	public List<CarInfo> getCarInfo() {

		return carRepository.findAll();
	}

	public CarInfo addCarInfo(CarInfo carInfo) {
		// TODO Auto-generated method stub
		return carRepository.save(carInfo);
	}
	public String cancelBooking(int tripBookingId) {
		logger.info("In Car Service to cancel the car seats booked for the tripId:{}",tripBookingId);

		CarBookingInfoAndStatus carBooked=carBookingRepo.findByTripBookingId(tripBookingId);
		int carId=carBooked.getCarBookingId();
		if(carBooked!=null) {
			carBookingRepo.deleteById(carId);
			logger.info("Deleting the car booking from our database for tripId:{}",tripBookingId);

			}
		return "Deleted";
	}
}
