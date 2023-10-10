package com.microservice.casestudy.CarBooking.event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.casestudy.CarBooking.entity.CarBookingInfoAndStatus;
import com.microservice.casestudy.CarBooking.entity.TourPackageAndStatus;
import com.microservice.casestudy.CarBooking.enums.TripBookingStatus;
import com.microservice.casestudy.CarBooking.service.CarService;
/**
 * 1.tourpackage info is received.take the tripbookingid and make booking according to it
	2.now take out the no of car seats to be booked and book the required no of seats
    3.when seats are booked then put a sucess msg in the car event
     4.if seats are not booked then keep a fail msg in the car event*
 
 */
 

@Service
public class TripBookingNotificationListener {
	
	@Autowired
	private CarService carService;
	@Value("${trip.topic.name}")
	private String topicName;
	
	@Autowired
	private CarBookingStatusEventPublisher carEventPublisher;
	ObjectMapper om=new ObjectMapper();
	Logger logger = LoggerFactory.getLogger(TripBookingNotificationListener.class);
	
	
	
	@KafkaListener(topics=("trip-booking-topic"),groupId="car-event")
	public void processCarBoooking(String tripBookingString) {
	      logger.info("Received a Notification from the topic: {} ,the details of request received are\n:{}",topicName,tripBookingString);

		try {
			TourPackageAndStatus tourpackage=om.readValue(tripBookingString, TourPackageAndStatus.class);
			logger.info("The tourPackage received is{}",tourpackage);
			if(tourpackage.getTripbookingStatus().equals(TripBookingStatus.TRIP_CREATED.toString())) 
			{
			int tripBookingId=tourpackage.getTripBookingId();
			int noOfCarSeatsNeeded=tourpackage.getNoOfcarSeats();
			CarBookingInfoAndStatus carBookingInfoAndStatus=carService.bookCar(tripBookingId,noOfCarSeatsNeeded);
			}
			else if(tourpackage.getTripbookingStatus().equals(TripBookingStatus.TRIP_CANCELLED)){
				int tripBookingId = tourpackage.getTripBookingId();
				String carBookingCancelled=carService.cancelBooking(tripBookingId);
				
			}
			
		}catch(JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
