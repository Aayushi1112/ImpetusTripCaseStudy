package com.microservice.casestudy.FlightBooking;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.casestudy.FlightBooking.entity.FlightBookingInfoAndStatus;
import com.microservice.casestudy.FlightBooking.entity.FlightInfo;
import com.microservice.casestudy.FlightBooking.enums.FlightBookingStatus;
import com.microservice.casestudy.FlightBooking.event.FlightBookingStatusEventPublisher;
import com.microservice.casestudy.FlightBooking.repository.FlightBookingRepository;
import com.microservice.casestudy.FlightBooking.repository.FlightRepository;
import com.microservice.casestudy.FlightBooking.service.FlightService;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {
	@Mock
	private FlightBookingRepository FlightBookingRepo;

	@Mock
	FlightRepository FlightRepository;
	@Mock
	FlightBookingStatusEventPublisher FlightEventPublisher;
	@Mock 
	 ObjectMapper om;
	
	@InjectMocks
	private FlightService FlightService;
	
	private FlightInfo Flight;
	

	@Test
	public void cancelBookingTest() {
		String callexpectation = "Deleted";
		FlightBookingInfoAndStatus mockFlightInfoStatus = mock(FlightBookingInfoAndStatus.class);
		mockFlightInfoStatus.setFlightBookingId(1);
		when(FlightBookingRepo.findByTripBookingId(1)).thenReturn(mockFlightInfoStatus);
        String actualCall = FlightService.cancelBooking(1);
		assertEquals(callexpectation, actualCall);

	}
	
	@Test
	public void addFlightInfoTest() {
	Flight=(new FlightInfo(1,"aa","aa",3));
		when(FlightRepository.save(Mockito.any())).thenReturn(Flight);
		FlightInfo FlightInfo = FlightService.addFlightInfo(Flight);
		assertEquals(1,FlightInfo.getFlightId());
		
}
	
	@Test
	public void bookFlightSuccessTest() throws JsonProcessingException {
		 Optional<FlightInfo> Flightinfo;
		 FlightInfo Flight=new FlightInfo();
		 FlightBookingInfoAndStatus bookedFlightInfoAndStatus=new FlightBookingInfoAndStatus();
			FlightBookingInfoAndStatus FlightBookedInfo = new FlightBookingInfoAndStatus();
			String createdFlightTripStr =new String();

		Flightinfo=Optional.ofNullable(new FlightInfo(1,"Winsor","200",300));
		 int noOfFlightBedsNeeded=1;
		Mockito.when( FlightRepository.findById(Mockito.anyInt())).thenReturn(Flightinfo);
		FlightBookingInfoAndStatus FlightBookedInfo1 = new FlightBookingInfoAndStatus();
		ArgumentCaptor<FlightBookingInfoAndStatus> captor=ArgumentCaptor.forClass(FlightBookingInfoAndStatus.class);
		
		Mockito.when(FlightBookingRepo.save(captor.capture())).thenReturn(FlightBookedInfo1);
		Mockito.doNothing().when(FlightEventPublisher).publish(createdFlightTripStr);
		Mockito.when(om.writeValueAsString(bookedFlightInfoAndStatus)).thenReturn(createdFlightTripStr);
		FlightBookingInfoAndStatus FlightBookingInfoAndStatus=FlightService.bookFlight(1,2);
		FlightBookingStatus status=FlightBookingStatus.FLIGHT_BOOKED;
		verify(FlightEventPublisher).publish(createdFlightTripStr);
}


}
