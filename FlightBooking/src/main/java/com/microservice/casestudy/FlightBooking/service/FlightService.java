package com.microservice.casestudy.FlightBooking.service;

import java.util.List;


import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.casestudy.FlightBooking.entity.FlightBookingInfoAndStatus;
import com.microservice.casestudy.FlightBooking.entity.FlightInfo;
import com.microservice.casestudy.FlightBooking.enums.FlightBookingStatus;
import com.microservice.casestudy.FlightBooking.event.FlightBookingStatusEventPublisher;
import com.microservice.casestudy.FlightBooking.repository.FlightBookingRepository;
import com.microservice.casestudy.FlightBooking.repository.FlightRepository;

/*
 * 1.here from the flight repo we have to find if required seats are available or not
   2.if yes then deduct no of seats in flight and update the flight repo and also
   3.update the booking repository with the corresponding tripbookingid
   4.else cancel do not book**/
@Service
public class FlightService {
	@Autowired
	FlightBookingStatusEventPublisher flightEventPublisher;
	@Autowired
	FlightRepository flightRepository;
	@Autowired
	FlightBookingRepository flightBookingRepo;
	ObjectMapper om = new ObjectMapper();
	Logger logger = LoggerFactory.getLogger(FlightService.class);


	private int FlightId = 1;

	public FlightBookingInfoAndStatus bookFlight(int tripBookingId, int noOfSeatsNeeded) throws JsonProcessingException {
		logger.info("In flight service and booking the flight for tripBookingId:{}", tripBookingId);

		Optional<FlightInfo> flightinfo=flightRepository.findById(FlightId);
		int noOfSeatsVacant=flightinfo.get().getNoOfSeatsAvailableInFlight();
		
		if(noOfSeatsNeeded< noOfSeatsVacant)
		{
			logger.info("Wonderful!!!We have the flight needed by you.Booking {} seats", noOfSeatsNeeded);

	       flightinfo.get().setNoOfSeatsAvailableInFlight(noOfSeatsVacant-noOfSeatsNeeded);
	       flightRepository.save(flightinfo.get());
	       
	       FlightBookingInfoAndStatus flightBookedInfo=new FlightBookingInfoAndStatus();
	       flightBookedInfo.setTripBookingId(tripBookingId);
	       flightBookedInfo.setFlightBookingStatus(FlightBookingStatus.FLIGHT_BOOKED);
	       flightBookedInfo.setNoOfFlightSeats(noOfSeatsNeeded);
	       FlightBookingInfoAndStatus bookedFlightInfoAndStatus=flightBookingRepo.save(flightBookedInfo);
			logger.info("Saved the flight booked details in the flight db:{}", bookedFlightInfoAndStatus);
			String createdFlightTripStr = om.writeValueAsString(bookedFlightInfoAndStatus);
			flightEventPublisher.publish(createdFlightTripStr);
			logger.info("Putting a FLIGHT_BOOKED message in the flight event  with info as:\n{}",createdFlightTripStr);
			
	       return bookedFlightInfoAndStatus;
	       }
		else
		{
			logger.info("Sorry!!!We dont have the flight seats needed by you.");

           FlightBookingInfoAndStatus bookedFlightInfoAndStatus=new FlightBookingInfoAndStatus();
           bookedFlightInfoAndStatus.setTripBookingId(tripBookingId);
           bookedFlightInfoAndStatus.setFlightBookingStatus(FlightBookingStatus.FLIGHT_BOOKING_CANCELLED);
           bookedFlightInfoAndStatus.setNoOfFlightSeats(0);
           String createdFlightTripStr = om.writeValueAsString(bookedFlightInfoAndStatus);
			
			String createdFlightBookedStr = om.writeValueAsString(createdFlightTripStr);
			flightEventPublisher.publish(createdFlightBookedStr);
			logger.info("Putting a FLIGHT_BOOKING_CANCELLED message in the flight event  with info as:\n {}",createdFlightTripStr);
        
		       return bookedFlightInfoAndStatus;
		}	
	}
	public String cancelBooking(int tripBookingId) {
		logger.info("In Flight Service to cancel the flight seats booked for the tripId:{}",tripBookingId);
FlightBookingInfoAndStatus flightBooked=flightBookingRepo.findByTripBookingId(tripBookingId);
		int flightId=flightBooked.getFlightBookingId();
		if(flightBooked!=null) {
			flightBookingRepo.deleteById(flightId);
			logger.info("Deleting the flight booking from our database for tripId:{}",tripBookingId);

			}
		return "Deleted";
	}
	public List<FlightInfo> getFlightInfo() {

		return flightRepository.findAll();
	}

	public FlightInfo addFlightInfo(FlightInfo flightInfo) {
		// TODO Auto-generated method stub
		return flightRepository.save(flightInfo);
	}
}
