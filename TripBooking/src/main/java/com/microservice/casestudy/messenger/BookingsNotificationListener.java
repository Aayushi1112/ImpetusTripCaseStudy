package com.microservice.casestudy.messenger;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.ContainerGroupSequencer;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.casestudy.entity.CarBookingInfoAndStatus;
import com.microservice.casestudy.entity.FlightBookingInfoAndStatus;
import com.microservice.casestudy.entity.HotelBookingInfoAndStatus;
import com.microservice.casestudy.entity.TourPackageAndStatus;
import com.microservice.casestudy.enums.CarBookingStatus;
import com.microservice.casestudy.enums.FlightBookingStatus;
import com.microservice.casestudy.enums.HotelBookingStatus;
import com.microservice.casestudy.enums.TripBookingStatus;
import com.microservice.casestudy.repository.TourPackageAndStatusRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookingsNotificationListener {

	@Value("${trip.topic.name}")
	private String topicName;

	@Autowired
	TripMessagePublisher tripMsgPublisher;

	@Autowired
	TourPackageAndStatusRepository packageRepository;

	ObjectMapper mapper = new ObjectMapper();
	Logger logger = LoggerFactory.getLogger(BookingsNotificationListener.class);
	int bookingno = 0;

	@KafkaListener(topics = { "hotel-booking-topic" }, groupId = "id1", containerGroup = "g1")
	public void checkHotelBoookingStatus(String hotelBookingString)
			throws JsonProcessingException, InterruptedException {
		Thread.sleep(8000);
		logger.info("TripBookingNotificationListener listening to Hotel Event.The Hotel Event message received is {}",
				hotelBookingString);
		HotelBookingInfoAndStatus hotelBookingInfo = mapper.readValue(hotelBookingString,
				HotelBookingInfoAndStatus.class);
		String hotelStatus = hotelBookingInfo.getHotelBookingStatus().toString();
		logger.info("Received the Hotel Booking status from HOTEL event as: {}", hotelStatus);
		Optional<TourPackageAndStatus> tourstatus = packageRepository.findById(hotelBookingInfo.getTripBookingId());
		logger.info("Hotel Status in Trip database was:{}", tourstatus.get().getHotelStatus());
		if (hotelStatus.equals(HotelBookingStatus.HOTEL_BOOKING_CANCELLED.toString())) {
			tourstatus.get().setHotelStatus(HotelBookingStatus.HOTEL_BOOKING_CANCELLED.toString());
			TourPackageAndStatus savedinfo = packageRepository.saveAndFlush(tourstatus.get());
			logger.info("Updated the hotel status in the Trip db as: {}", savedinfo.getHotelStatus());
			logger.info("Cancelling hotel booking");
			cancelTrip(hotelBookingInfo.getTripBookingId());
		} else if (hotelStatus.equals(HotelBookingStatus.HOTEL_BOOKED.toString())) {
			logger.info("Received the Hotel Booking status from HOTEL event as: {}", hotelStatus);
			logger.info("Hotel Status in Trip database was:{}", tourstatus.get().getHotelStatus());
			tourstatus.get().setHotelStatus(HotelBookingStatus.HOTEL_BOOKED.toString());
			TourPackageAndStatus savedinfo = packageRepository.saveAndFlush(tourstatus.get());
			logger.info("Updated the hotel status in the trip db as: {}", savedinfo.getHotelStatus());
			updateTripBookingDb(hotelBookingInfo.getTripBookingId());
		}
	}

	@KafkaListener(topics = { "flight-booking-topic" }, groupId = "id2", containerGroup = "g1")
	public void checkFlightBoookingStatus(String flightBookingString)
			throws JsonMappingException, JsonProcessingException, InterruptedException {
		Thread.sleep(10000);
		logger.info("TripBookingNotificationListener listening to Flight Event.The Flight Event message received is {}",
				flightBookingString);
		FlightBookingInfoAndStatus flightBookingInfo = mapper.readValue(flightBookingString,
				FlightBookingInfoAndStatus.class);
		String flightStatus = flightBookingInfo.getFlightBookingStatus().toString();
		logger.info("Received the Flight Booking status from FLIGHT Topic as: {}", flightStatus);
		Optional<TourPackageAndStatus> tourstatus = packageRepository.findById(flightBookingInfo.getTripBookingId());
		logger.info("Flight Status in Trip database was:{}", tourstatus.get().getFlightStatus());
		if (flightStatus.equals(FlightBookingStatus.FLIGHT_BOOKING_CANCELLED.toString())) {
			tourstatus.get().setFlightStatus(FlightBookingStatus.FLIGHT_BOOKING_CANCELLED.toString());
			TourPackageAndStatus savedinfo = packageRepository.saveAndFlush(tourstatus.get());
			logger.info("Updated the flight status in the Trip db as: {}", savedinfo.getFlightStatus());
			logger.info("Cancelling flight booking");
			cancelTrip(flightBookingInfo.getTripBookingId());
		} else if (flightStatus.equals(FlightBookingStatus.FLIGHT_BOOKED.toString())) {
			logger.info("Received the Flight Booking status from FLIGHT event as: {}", flightStatus);
			logger.info("Flight Status in Trip database was:{}", tourstatus.get().getFlightStatus());
			tourstatus.get().setFlightStatus(FlightBookingStatus.FLIGHT_BOOKED.toString());
			TourPackageAndStatus savedinfo = packageRepository.saveAndFlush(tourstatus.get());
			logger.info("Updated the flight status in the trip db as: {}", savedinfo.getFlightStatus());
			updateTripBookingDb(flightBookingInfo.getTripBookingId());
		}
	}

	@KafkaListener(topics = { "car-booking-topic" }, groupId = "id3", containerGroup = "g1")
	public void checkCarBoookingStatus(String carBookingString)
			throws JsonMappingException, JsonProcessingException, InterruptedException {
		logger.info("TripBookingNotificationListener listening to Car Event.The Car Event message received is {}",
				carBookingString);
		CarBookingInfoAndStatus carBookingInfo = mapper.readValue(carBookingString, CarBookingInfoAndStatus.class);
		String carStatus = carBookingInfo.getCarBookingStatus().toString();
		logger.info("Received the Car booking status from Car Topic as: {}", carStatus);
		Optional<TourPackageAndStatus> tourstatus = packageRepository.findById(carBookingInfo.getTripBookingId());
		logger.info("Car Status in Trip database was:{}", tourstatus.get().getCarStatus());
		if (carStatus.equals(CarBookingStatus.CAR_BOOKING_CANCELLED.toString())) {
			tourstatus.get().setCarStatus(CarBookingStatus.CAR_BOOKING_CANCELLED.toString());
			TourPackageAndStatus savedinfo = packageRepository.saveAndFlush(tourstatus.get());
			logger.info("Updated the Car status in the trip db as: {}", savedinfo.getCarStatus());
			logger.info("Cancelling car booking");
			cancelTrip(carBookingInfo.getTripBookingId());
		} else if (carStatus.equals(CarBookingStatus.CAR_BOOKED.toString())) {
			logger.info("Received the Car Booking status from Car topic as: {}", carStatus);
			logger.info("Car Status in Trip database was:{}", tourstatus.get().getCarStatus());
			tourstatus.get().setCarStatus(CarBookingStatus.CAR_BOOKED.toString());
			TourPackageAndStatus savedinfo = packageRepository.saveAndFlush(tourstatus.get());
			logger.info("Updated the car status in the trip db as: {}", savedinfo.getCarStatus());
			updateTripBookingDb(carBookingInfo.getTripBookingId());
		}
	}

	@Bean
	ContainerGroupSequencer sequencer(KafkaListenerEndpointRegistry registry) {
		return new ContainerGroupSequencer(registry, 20000, "g1");
	}

	private void cancelTrip(int tripBookingId) throws JsonProcessingException {
		logger.info("CANCEL TRIP Command received");
		Optional<TourPackageAndStatus> tourPackageAndStatus = packageRepository.findById(tripBookingId);
		if (tourPackageAndStatus.get().getTripbookingStatus().equals(TripBookingStatus.TRIP_CREATED.toString())) {
			logger.info("Initiating the trip cancellation");
			tourPackageAndStatus.get().setTripbookingStatus(TripBookingStatus.TRIP_CANCELLED.toString());
			TourPackageAndStatus tourPackageAndStatusUpdated = packageRepository.save(tourPackageAndStatus.get());
			logger.info("Updated the Trip db with trip status as:{}",
					tourPackageAndStatusUpdated.getTripbookingStatus());
			TourPackageAndStatus tripStatus = new TourPackageAndStatus();
			tripStatus.setTripBookingId(tripBookingId);
			tripStatus.setTripbookingStatus(TripBookingStatus.TRIP_CANCELLED.toString());
			String createdTripStr = mapper.writeValueAsString(tripStatus);
			logger.info("Sending a trip cancellation message in the trip event");
			tripMsgPublisher.publish(createdTripStr);
		}
	}
      private void updateTripBookingDb(int tripBookingId) {
		Optional<TourPackageAndStatus> tourPackageAndStatus = packageRepository.findById(tripBookingId);
		logger.info("Before Updating the booking status the values received from trip db are :{}",
				tourPackageAndStatus.get().toString());
		if (tourPackageAndStatus.get().getTripbookingStatus().equals(TripBookingStatus.TRIP_CREATED.toString())) {
			if (tourPackageAndStatus.get().getCarStatus().equals(CarBookingStatus.CAR_BOOKED.toString())
					&& tourPackageAndStatus.get().getFlightStatus().equals(FlightBookingStatus.FLIGHT_BOOKED.toString())
					&& tourPackageAndStatus.get().getHotelStatus().equals(HotelBookingStatus.HOTEL_BOOKED.toString())) {
				logger.info("FINALLY !! YOUR TRIP BOOKING IS SUCCESSFUL");
				tourPackageAndStatus.get().setTripbookingStatus(TripBookingStatus.TRIP_BOOKED.toString());
			}
		 //else {logger.info("TRIP BOOKING STILL PENDING OR MIGHT BE cancelled ");}

		}

	}
}
