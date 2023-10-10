package com.microservice.casestudy.CarBooking;

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
import com.microservice.casestudy.CarBooking.entity.CarBookingInfoAndStatus;
import com.microservice.casestudy.CarBooking.entity.CarInfo;
import com.microservice.casestudy.CarBooking.enums.CarBookingStatus;
import com.microservice.casestudy.CarBooking.event.CarBookingStatusEventPublisher;
import com.microservice.casestudy.CarBooking.repository.CarBookingRepository;
import com.microservice.casestudy.CarBooking.repository.CarRepository;
import com.microservice.casestudy.CarBooking.service.CarService;
@ExtendWith(MockitoExtension.class)
public class ClassServiceTest {

	@Mock
	private CarBookingRepository CarBookingRepo;

	@Mock
	CarRepository CarRepository;
	@Mock
	CarBookingStatusEventPublisher CarEventPublisher;
	@Mock 
	 ObjectMapper om;
	
	@InjectMocks
	private CarService CarService;
	
	private CarInfo Car;
	

	@Test
	public void cancelBookingTest() {
		String callexpectation = "Deleted";
		CarBookingInfoAndStatus mockCarInfoStatus = mock(CarBookingInfoAndStatus.class);
		mockCarInfoStatus.setCarBookingId(1);
		when(CarBookingRepo.findByTripBookingId(1)).thenReturn(mockCarInfoStatus);
        String actualCall = CarService.cancelBooking(1);
		assertEquals(callexpectation, actualCall);

	}
	
	@Test
	public void addCarInfoTest() {
	Car=(new CarInfo(1,"aa","aa",3));
		when(CarRepository.save(Mockito.any())).thenReturn(Car);
		CarInfo CarInfo = CarService.addCarInfo(Car);
		assertEquals(1,CarInfo.getCarId());
		
}
	
	@Test
	public void bookCarSuccessTest() throws JsonProcessingException {
		 Optional<CarInfo> Carinfo;
		 CarInfo Car=new CarInfo();
		 CarBookingInfoAndStatus bookedCarInfoAndStatus=new CarBookingInfoAndStatus();
			CarBookingInfoAndStatus CarBookedInfo = new CarBookingInfoAndStatus();
			String createdCarTripStr =new String();

		Carinfo=Optional.ofNullable(new CarInfo(1,"Winsor","200",300));
		 int noOfCarBedsNeeded=1;
		Mockito.when( CarRepository.findById(Mockito.anyInt())).thenReturn(Carinfo);
		CarBookingInfoAndStatus CarBookedInfo1 = new CarBookingInfoAndStatus();
		ArgumentCaptor<CarBookingInfoAndStatus> captor=ArgumentCaptor.forClass(CarBookingInfoAndStatus.class);
		
		Mockito.when(CarBookingRepo.save(captor.capture())).thenReturn(CarBookedInfo1);
		Mockito.doNothing().when(CarEventPublisher).publish(createdCarTripStr);
		Mockito.when(om.writeValueAsString(bookedCarInfoAndStatus)).thenReturn(createdCarTripStr);
		CarBookingInfoAndStatus CarBookingInfoAndStatus=CarService.bookCar(1,2);
		CarBookingStatus status=CarBookingStatus.CAR_BOOKED;
		verify(CarEventPublisher).publish(createdCarTripStr);
}


}

