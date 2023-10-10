package com.microservice.casestudy.HotelBooking.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.casestudy.HotelBooking.entity.HotelBookingInfoAndStatus;
import com.microservice.casestudy.HotelBooking.entity.HotelInfo;
import com.microservice.casestudy.HotelBooking.enums.HotelBookingStatus;
import com.microservice.casestudy.HotelBooking.event.HotelBookingStatusEventPublisher;
import com.microservice.casestudy.HotelBooking.repository.HotelBookingRepository;
import com.microservice.casestudy.HotelBooking.repository.HotelRepository;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {

	@Mock
	private HotelBookingRepository hotelBookingRepo;

	@Mock
	HotelRepository hotelRepository;
	@Mock
	HotelBookingStatusEventPublisher hotelEventPublisher;
	@Mock 
	 ObjectMapper om;
	
	@InjectMocks
	private HotelService hotelService;
	
	private HotelInfo hotel;
	

	@Test
	public void cancelBookingTest() {
		String callexpectation = "Deleted";
		HotelBookingInfoAndStatus mockHotelInfoStatus = mock(HotelBookingInfoAndStatus.class);
		mockHotelInfoStatus.setHotelBookingId(1);
		when(hotelBookingRepo.findByTripBookingId(1)).thenReturn(mockHotelInfoStatus);
        String actualCall = hotelService.cancelBooking(1);
		assertEquals(callexpectation, actualCall);

	}
	
	@Test
	public void addHotelInfoTest() {
	hotel=(new HotelInfo(1,"aa","aa",3));
		when(hotelRepository.save(Mockito.any())).thenReturn(hotel);
		HotelInfo hotelInfo = hotelService.addHotelInfo(hotel);
		assertEquals(1,hotelInfo.getHotelId());
		
}
	
	@Test
	public void bookHotelSuccessTest() throws JsonProcessingException {
		 Optional<HotelInfo> hotelinfo;
		 HotelInfo hotel=new HotelInfo();
		 HotelBookingInfoAndStatus bookedHotelInfoAndStatus=new HotelBookingInfoAndStatus();
			HotelBookingInfoAndStatus hotelBookedInfo = new HotelBookingInfoAndStatus();
			String createdHotelTripStr =new String();

		hotelinfo=Optional.ofNullable(new HotelInfo(1,"Winsor","200",300));
		 int noOfHotelBedsNeeded=1;
		Mockito.when( hotelRepository.findById(Mockito.anyInt())).thenReturn(hotelinfo);
		HotelBookingInfoAndStatus hotelBookedInfo1 = new HotelBookingInfoAndStatus();
		ArgumentCaptor<HotelBookingInfoAndStatus> captor=ArgumentCaptor.forClass(HotelBookingInfoAndStatus.class);
		
		Mockito.when(hotelBookingRepo.save(captor.capture())).thenReturn(hotelBookedInfo1);
		Mockito.doNothing().when(hotelEventPublisher).publish(createdHotelTripStr);
		Mockito.when(om.writeValueAsString(bookedHotelInfoAndStatus)).thenReturn(createdHotelTripStr);
		HotelBookingInfoAndStatus hotelBookingInfoAndStatus=hotelService.bookHotel(1,2);
		HotelBookingStatus status=HotelBookingStatus.HOTEL_BOOKED;
		verify(hotelEventPublisher).publish(createdHotelTripStr);
}

}
