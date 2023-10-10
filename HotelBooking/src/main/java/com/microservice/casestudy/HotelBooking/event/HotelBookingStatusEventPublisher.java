package com.microservice.casestudy.HotelBooking.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.casestudy.HotelBooking.entity.HotelBookingInfoAndStatus;
import com.microservice.casestudy.HotelBooking.service.HotelService;
@Component
public class HotelBookingStatusEventPublisher {
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	@Value("${hotel.topic.name}")
	private String topicHotel;
	Logger logger = LoggerFactory.getLogger(HotelBookingStatusEventPublisher.class);

	ObjectMapper om = new ObjectMapper();
	public void publish(String createdHotelTripStr) throws JsonProcessingException {
	logger.info("publishinhg hotel status to the topic:{}",topicHotel);
		kafkaTemplate.send(topicHotel, createdHotelTripStr);
		
	}
	
	
	
	
	
	
}
