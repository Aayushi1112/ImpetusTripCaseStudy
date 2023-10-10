package com.microservice.casestudy.CarBooking;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.casestudy.CarBooking.controller.CarController;
import com.microservice.casestudy.CarBooking.entity.CarBookingInfoAndStatus;
import com.microservice.casestudy.CarBooking.entity.CarInfo;
import com.microservice.casestudy.CarBooking.entity.TourPackageAndStatus;
import com.microservice.casestudy.CarBooking.service.CarService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private CarService carService;

	@Autowired
private CarController carController;

	CarInfo car1 = new CarInfo(1, "Evince", "233", 4);
	CarInfo car2 = new CarInfo();

	@Test
	public void welcome() throws Exception {
		ResultActions response = mockMvc.perform(get("/cars/welcome"));
		response.andExpect(status().isOk()).andExpect(content().string("Welcome to the car service"));
		// .andExpect(jsonPath("$"));

	}

	@Test
	public void getcarInfoTest() throws Exception {
		List<CarInfo> cars = new ArrayList();
		cars.add(car1);
		cars.add(car2);
		Mockito.when(carService.getCarInfo()).thenReturn(cars);
		ResultActions response = mockMvc.perform(get("/cars/get-car-info"));
		response.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
		// .andExpect((ResultMatcher) jsonPath("$", isA(ArrayList.class)));
	}

	@Test
	public void addcarInfoTest() throws JsonProcessingException, Exception {
		Mockito.when(carService.addCarInfo(car1)).thenReturn(car1);
		ResultActions response = mockMvc.perform(post("/cars/add-car-info").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(car1)));
		response.andExpect(status().isOk());
	}
	
	@Test
	public void bookcarTest() throws Exception {
	CarBookingInfoAndStatus carInfo=new CarBookingInfoAndStatus();
		
		TourPackageAndStatus tourPackage=new TourPackageAndStatus();
		tourPackage.setTripBookingId(1);
		tourPackage.setNoOfcarSeats(1);
		Mockito.when(carService.bookCar(Mockito.anyInt(),Mockito.anyInt())).thenReturn(carInfo);
		ResultActions response=mockMvc.perform(post("/cars/book-a-car")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(tourPackage)));
		response.andExpect(status().isOk());
		
		
	}
	



}
