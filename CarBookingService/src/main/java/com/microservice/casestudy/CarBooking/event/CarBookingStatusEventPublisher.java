package com.microservice.casestudy.CarBooking.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.casestudy.CarBooking.entity.CarBookingInfoAndStatus;
@Component
public class CarBookingStatusEventPublisher {
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	@Value("${car.topic.name}")
	private String topicName;
	ObjectMapper om = new ObjectMapper();
	public void publish(String carBookingInfoAndStatus) throws JsonProcessingException {
		//String createdCarBookedStr = om.writeValueAsString(carBookingInfoAndStatus);
		kafkaTemplate.send(topicName, carBookingInfoAndStatus);
		
	}
	
	
	
	
	
	
}
