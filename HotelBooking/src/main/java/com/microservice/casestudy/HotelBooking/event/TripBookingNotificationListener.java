package com.microservice.casestudy.HotelBooking.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.casestudy.HotelBooking.entity.HotelBookingInfoAndStatus;
import com.microservice.casestudy.HotelBooking.entity.TourPackageAndStatus;
import com.microservice.casestudy.HotelBooking.enums.TripBookingStatus;
import com.microservice.casestudy.HotelBooking.service.HotelService;
/*
 * 1.tourpackage info is received.take the tripbookingid and make booking
	according to it
	2.now take out the no of hotels to be booked and book the required no of rooms
			// in your hotel
	3.when rooms are booked then put a sucess msg in the hotelevent
	 4.if rooms are not booked then keep a fail msg in the hotel event
	 */

@Service
public class TripBookingNotificationListener {

	@Autowired
	private HotelService hotelService;
	@Value("${trip.topic.name}")
	private String topicName;

	@Autowired
	private HotelBookingStatusEventPublisher hotelEventPublisher;
	ObjectMapper om = new ObjectMapper();

	Logger logger = LoggerFactory.getLogger(TripBookingNotificationListener.class);

	@KafkaListener(topics = { "trip-booking-topic" }, groupId = "hotel-event")
	public void processHotelBoooking(String tripBookingString) {
      logger.info("Received a Notification from the topic: {} ,the details of request received are\n:{}",topicName,tripBookingString);
		try {
			TourPackageAndStatus tourpackage = om.readValue(tripBookingString, TourPackageAndStatus.class);
			logger.info("TOUR STATUS IS{}",tourpackage.getTripbookingStatus().toString());
			boolean val=tourpackage.getTripbookingStatus().equals(TripBookingStatus.TRIP_CREATED.toString());
			logger.info("Boolean val is {}",val);
			
			if (val) {
				logger.info("TRIP_CREATED request is received.");
				int tripBookingId = tourpackage.getTripBookingId();
				int noOfHotelBeds = tourpackage.getNoOfhotelBeds();
				HotelBookingInfoAndStatus hotelBookingInfoAndStatus = hotelService.bookHotel(tripBookingId,
						noOfHotelBeds);
			} else if (tourpackage.getTripbookingStatus().equals(TripBookingStatus.TRIP_CANCELLED)) {
				logger.info("Got a trip cancellation message for tripId:{}",tourpackage.getTripBookingId());
				int tripBookingId = tourpackage.getTripBookingId();
				String hotelBookingCancelled = hotelService.cancelBooking(tripBookingId);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
