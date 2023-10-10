package com.microservice.casestudy.FlightBooking;

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
import com.microservice.casestudy.FlightBooking.controller.FlightController;
import com.microservice.casestudy.FlightBooking.entity.FlightBookingInfoAndStatus;
import com.microservice.casestudy.FlightBooking.entity.FlightInfo;
import com.microservice.casestudy.FlightBooking.entity.TourPackageAndStatus;
import com.microservice.casestudy.FlightBooking.service.FlightService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FlightControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private FlightService FlightService;

	@Autowired
private FlightController FlightController;

	FlightInfo Flight1 = new FlightInfo(1, "Evince", "233", 4);
	FlightInfo Flight2 = new FlightInfo();

	@Test
	public void welcome() throws Exception {
		ResultActions response = mockMvc.perform(get("/flights/welcome"));
		response.andExpect(status().isOk()).andExpect(content().string("Welcome to the flight service"));
		// .andExpect(jsonPath("$"));

	}

	@Test
	public void getFlightInfoTest() throws Exception {
		List<FlightInfo> Flights = new ArrayList();
		Flights.add(Flight1);
		Flights.add(Flight2);
		Mockito.when(FlightService.getFlightInfo()).thenReturn(Flights);
		ResultActions response = mockMvc.perform(get("/flights/get-flight-info"));
		response.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
		// .andExpect((ResultMatcher) jsonPath("$", isA(ArrayList.class)));
	}

	@Test
	public void addFlightInfoTest() throws JsonProcessingException, Exception {
		Mockito.when(FlightService.addFlightInfo(Flight1)).thenReturn(Flight1);
		ResultActions response = mockMvc.perform(post("/flights/add-flight-info").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(Flight1)));
		response.andExpect(status().isOk());
	}
	
	@Test
	public void bookFlightTest() throws Exception {
		FlightBookingInfoAndStatus FlightInfo=new FlightBookingInfoAndStatus();
		
		TourPackageAndStatus tourPackage=new TourPackageAndStatus();
		tourPackage.setTripBookingId(1);
		tourPackage.setNoOfflightSeats(1);
		Mockito.when(FlightService.bookFlight(Mockito.anyInt(),Mockito.anyInt())).thenReturn(FlightInfo);
		ResultActions response=mockMvc.perform(post("/flights/book-a-flight")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(tourPackage)));
		response.andExpect(status().isOk());
		
		
	}
	


}
