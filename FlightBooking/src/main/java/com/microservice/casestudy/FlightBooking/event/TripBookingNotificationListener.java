package com.microservice.casestudy.FlightBooking.event;
import org.slf4j.Logger;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.casestudy.FlightBooking.entity.FlightBookingInfoAndStatus;
import com.microservice.casestudy.FlightBooking.entity.TourPackageAndStatus;
import com.microservice.casestudy.FlightBooking.enums.TripBookingStatus;
import com.microservice.casestudy.FlightBooking.service.FlightService;
/**
 * 1.tourpackage info is received.take the tripbookingid and make booking according to it
	2.now take out the no of seats to be booked and book the required no of seats in your flight
	3.when seats are booked then put a sucess msg in the flightevent
	4.if seats are not booked then keep a fail msg in the flight event*/

@Service
public class TripBookingNotificationListener {
	
	//private static final String trip_booking_topic = null;

	@Autowired
	private FlightService flightService;
	@Value("${trip.topic.name}")
	private String topicName;
	
	@Autowired
	private FlightBookingStatusEventPublisher flightEventPublisher;
	ObjectMapper om=new ObjectMapper();
	Logger logger = LoggerFactory.getLogger(TripBookingNotificationListener.class);
	
	@KafkaListener(topics=("trip-booking-topic"),groupId="flight-event")
	public void processFlightBoooking( String  tripBookingString) {
	      logger.info("Received a Notification from the topic: {} ,the details of request received are\n:{}",topicName,tripBookingString);
		try {
			TourPackageAndStatus tourpackage=om.readValue(tripBookingString, TourPackageAndStatus.class);
			logger.info("TRIP STATUS{}",tourpackage.getTripbookingStatus());
			if(tourpackage.getTripbookingStatus().equals(TripBookingStatus.TRIP_CREATED.toString())) 
			{
			logger.info("TRIP_CREATED request is received.");
			int tripBookingId=tourpackage.getTripBookingId();
			int noOfSeats=tourpackage.getNoOfflightSeats();
			FlightBookingInfoAndStatus flightBookingInfoAndStatus=flightService.bookFlight(tripBookingId,noOfSeats);}
			//String createdFlightBookedStr = om.writeValueAsString(flightBookingInfoAndStatus);
			//flightEventPublisher.publish(createdFlightBookedStr)
			else if(tourpackage.getTripbookingStatus().equals(TripBookingStatus.TRIP_CANCELLED)){
				int tripBookingId = tourpackage.getTripBookingId();
				logger.info("Got a trip cancellation message for tripId:{}",tourpackage.getTripBookingId());

				String flightBookingCancelled=flightService.cancelBooking(tripBookingId);
				
			}
			
			
		}catch(JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
