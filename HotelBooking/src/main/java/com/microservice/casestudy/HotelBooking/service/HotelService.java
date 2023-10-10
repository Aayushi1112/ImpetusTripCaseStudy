package com.microservice.casestudy.HotelBooking.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.casestudy.HotelBooking.entity.HotelBookingInfoAndStatus;
import com.microservice.casestudy.HotelBooking.entity.HotelInfo;
import com.microservice.casestudy.HotelBooking.enums.HotelBookingStatus;
import com.microservice.casestudy.HotelBooking.event.HotelBookingStatusEventPublisher;
import com.microservice.casestudy.HotelBooking.repository.HotelBookingRepository;
import com.microservice.casestudy.HotelBooking.repository.HotelRepository;

/**
 * //here from the hotel repo we have to find if required beds are available or
 * not //if yes then deduct no of beds in hotel and update the hotel repo and
 * also //update the booking repository with the corresponding tripbookingid
 * //else cancel do not book
 */
@Service
public class HotelService {
	@Autowired
	HotelRepository hotelRepository;
	@Autowired
	HotelBookingRepository hotelBookingRepo;

	@Autowired
	private HotelBookingStatusEventPublisher hotelEventPublisher;

	private int HotelId = 1;
	ObjectMapper om = new ObjectMapper();
	Logger logger = LoggerFactory.getLogger(HotelService.class);

	public HotelBookingInfoAndStatus bookHotel(int tripBookingId, int noOfHotelBedsNeeded)
			throws JsonProcessingException {
		logger.info("In hotel service and booking the hotel for tripBookingId:{}", tripBookingId);

		Optional<HotelInfo> hotelinfo = hotelRepository.findById(HotelId);
		int noOfBedsVacant = hotelinfo.get().getNoOfBedsAvailableInHotel();

		if (noOfHotelBedsNeeded < noOfBedsVacant) {
			logger.info("Wonderful!!!We have the hotel beds needed by you.Booking {} rooms", noOfHotelBedsNeeded);
			hotelinfo.get().setNoOfBedsAvailableInHotel(noOfBedsVacant - noOfHotelBedsNeeded);
			hotelRepository.save(hotelinfo.get());

			HotelBookingInfoAndStatus hotelBookedInfo = new HotelBookingInfoAndStatus();
			hotelBookedInfo.setTripBookingId(tripBookingId);
			hotelBookedInfo.setHotelBookingStatus(HotelBookingStatus.HOTEL_BOOKED);
			hotelBookedInfo.setNoOfHotelBeds(noOfHotelBedsNeeded);
			HotelBookingInfoAndStatus bookedHotelInfoAndStatus = hotelBookingRepo.save(hotelBookedInfo);
			logger.info("Saved the hotel booked details in the hotel db:{}", bookedHotelInfoAndStatus);
           String createdHotelTripStr = om.writeValueAsString(bookedHotelInfoAndStatus);
			hotelEventPublisher.publish(createdHotelTripStr);
			logger.info("Putting a HOTEL_BOOKED message in the hotel event  with info as:\n{}",createdHotelTripStr);
			return bookedHotelInfoAndStatus;
		} else {
			logger.info("Sorry!!!We dont have the hotel beds needed by you.");
			HotelBookingInfoAndStatus bookedHotelInfoAndStatus = new HotelBookingInfoAndStatus();
			bookedHotelInfoAndStatus.setTripBookingId(tripBookingId);
			bookedHotelInfoAndStatus.setHotelBookingStatus(HotelBookingStatus.HOTEL_BOOKING_CANCELLED);
			bookedHotelInfoAndStatus.setNoOfHotelBeds(0);
			String createdHotelTripStr = om.writeValueAsString(bookedHotelInfoAndStatus);
			hotelEventPublisher.publish(createdHotelTripStr);
			logger.info("Putting a HOTEL_BOOKING_CANCELLED message in the hotel event  with info as:\n{}",createdHotelTripStr);
         return bookedHotelInfoAndStatus;
		}
	}

	public List<HotelInfo> getHotelInfo() {

		return hotelRepository.findAll();
	}

	public HotelInfo addHotelInfo(HotelInfo hotelInfo) {
		// TODO Auto-generated method stub
		return hotelRepository.save(hotelInfo);
	}

	public String cancelBooking(int tripBookingId) {
		logger.info("In Hotel Service to cancel the hotel rooms booked for the tripId:{}",tripBookingId);
		HotelBookingInfoAndStatus hotelBooked=hotelBookingRepo.findByTripBookingId(tripBookingId);
		int hotelId=hotelBooked.getHotelBookingId();
		if(hotelBooked!=null) {
			logger.info("Deleting the hotel booking from our database for tripId:{}",tripBookingId);
			//hotelBookingRepo.deleteById(hotelId);
			}
		return "Deleted";
	}
	
	
}
