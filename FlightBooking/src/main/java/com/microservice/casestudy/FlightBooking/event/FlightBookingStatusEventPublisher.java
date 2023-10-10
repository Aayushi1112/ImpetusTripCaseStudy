package com.microservice.casestudy.FlightBooking.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.casestudy.FlightBooking.entity.FlightBookingInfoAndStatus;
@Component
public class FlightBookingStatusEventPublisher {
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	@Value("${flight.topic.name}")
	private String topicName;
	ObjectMapper om = new ObjectMapper();
	public void publish(String createdFlightTripStr) throws JsonProcessingException {
		//String createdFlightBookedStr = om.writeValueAsString(createdFlightTripStr);
		kafkaTemplate.send(topicName, createdFlightTripStr);
		
	}
	
	
	
	
	
	
}
