package com.microservice.casestudy.messenger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.microservice.casestudy.controller.TripBookingController;
@Component
public class TripMessagePublisher {
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;
	Logger logger= LoggerFactory.getLogger(TripMessagePublisher.class);
	@Value("${trip.topic.name}")
	private String tripBookTopic;
	
	
	public void publish(String createdTripStr) {
		logger.info("SEnding message to {}",tripBookTopic);
		kafkaTemplate.send(tripBookTopic,createdTripStr);
		
	}
	

}
