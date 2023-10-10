package com.microservice.casestudy.servcie;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.casestudy.entity.CarInfo;
import com.microservice.casestudy.entity.FlightInfo;
import com.microservice.casestudy.entity.HotelInfo;
import com.microservice.casestudy.entity.TourPackageAndStatus;
import com.microservice.casestudy.entity.TripCatalog;
import com.microservice.casestudy.entity.TripServices;
import com.microservice.casestudy.enums.CarBookingStatus;
import com.microservice.casestudy.enums.FlightBookingStatus;
import com.microservice.casestudy.enums.HotelBookingStatus;
import com.microservice.casestudy.enums.TripBookingStatus;
import com.microservice.casestudy.feignproxies.CarProxy;
import com.microservice.casestudy.feignproxies.FlightProxy;
import com.microservice.casestudy.feignproxies.HotelProxy;
import com.microservice.casestudy.messenger.TripMessagePublisher;
import com.microservice.casestudy.repository.TourPackageAndStatusRepository;
//import com.microservice.casestudy.entity.TripCatalog;
import com.microservice.casestudy.repository.TripCatalogRepository;

@Service
public class TripBookingService {

	@Autowired
	TripCatalogRepository tripCatalogrepository;
	@Autowired
	TourPackageAndStatusRepository packageRepository;
	
	@Autowired
    CarProxy carProxy;
	
	
	@Autowired
    FlightProxy flightProxy;
	
	@Autowired
    HotelProxy hotelProxy;
	
	
	
	@Autowired 
	private TripMessagePublisher tripMsgPublisher;

    ObjectMapper om = new ObjectMapper();
	Logger logger= LoggerFactory.getLogger(TripBookingService.class);

	public List<TripCatalog> getTripCatalog() {
		List<TripCatalog> trips = tripCatalogrepository.findAll();
		return trips;
	}

	public String addTripCatalog(TripCatalog tripCatalog) {
		TripCatalog trip = tripCatalogrepository.save(tripCatalog);
		return "Tour Package added.The id is: " + trip.getId();
	}

	public TourPackageAndStatus bookTrip(TourPackageAndStatus tripInclusion) throws JsonProcessingException {
		logger.info("In TripBookingService service I am booking the tour");
		tripInclusion.setTripbookingStatus(TripBookingStatus.TRIP_CREATED.toString());
		tripInclusion.setFlightStatus(FlightBookingStatus.FLIGHT_BOOKING_INITIATED.toString());
		tripInclusion.setHotelStatus(HotelBookingStatus.HOTEL_BOOKING_INITIATED.toString());
		tripInclusion.setCarStatus(CarBookingStatus.CAR_BOOKING_INITIATED.toString());
		TourPackageAndStatus tourStatus = packageRepository.save(tripInclusion);
		logger.info("Saving the above created trip into the db {}",tourStatus);
		String createdTripStr = om.writeValueAsString(tourStatus);
		logger.info("Sending an event in the trip booking topic  as {}",createdTripStr);
		logger.info("The above info will be listened by our car,flight and hotel services");
		tripMsgPublisher.publish(createdTripStr);
		return tourStatus;
	}

	public List<HotelInfo> getTripHotel() {
		
		List<HotelInfo> hotelInfo=hotelProxy.getHotelInfo();
		return hotelInfo;
	}
	
	
public List<CarInfo> getTripCar() {
		
		List<CarInfo> carInfo=carProxy.getCarInfo();
		return carInfo;
	}
public List<FlightInfo> getTripFlight() {
		
		List<FlightInfo> flightInfo=flightProxy.getFlightInfo();
		return flightInfo;
	}

public TourPackageAndStatus getTripStatus(int tripBookingId) {
	Optional<TourPackageAndStatus> tourInfo=packageRepository.findById(tripBookingId);
	return tourInfo.get();
}
}
