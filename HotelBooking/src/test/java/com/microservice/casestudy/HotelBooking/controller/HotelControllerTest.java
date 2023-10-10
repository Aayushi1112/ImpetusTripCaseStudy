package com.microservice.casestudy.HotelBooking.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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
import com.microservice.casestudy.HotelBooking.entity.HotelBookingInfoAndStatus;
import com.microservice.casestudy.HotelBooking.entity.HotelInfo;
import com.microservice.casestudy.HotelBooking.entity.TourPackageAndStatus;
import com.microservice.casestudy.HotelBooking.service.HotelService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HotelControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private HotelService hotelService;

	@Autowired
private HotelController hotelController;

	HotelInfo hotel1 = new HotelInfo(1, "Evince", "233", 4);
	HotelInfo hotel2 = new HotelInfo();

	@Test
	public void welcome() throws Exception {
		ResultActions response = mockMvc.perform(get("/hotels/welcome"));
		response.andExpect(status().isOk()).andExpect(content().string("Welcome to the hotel service"));
		// .andExpect(jsonPath("$"));

	}

	@Test
	public void getHotelInfoTest() throws Exception {
		List<HotelInfo> hotels = new ArrayList();
		hotels.add(hotel1);
		hotels.add(hotel2);
		Mockito.when(hotelService.getHotelInfo()).thenReturn(hotels);
		ResultActions response = mockMvc.perform(get("/hotels/get-hotel-info"));
		response.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
		// .andExpect((ResultMatcher) jsonPath("$", isA(ArrayList.class)));
	}

	@Test
	public void addHotelInfoTest() throws JsonProcessingException, Exception {
		Mockito.when(hotelService.addHotelInfo(hotel1)).thenReturn(hotel1);
		ResultActions response = mockMvc.perform(post("/hotels/add-hotel-info").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(hotel1)));
		response.andExpect(status().isOk());
	}
	
	@Test
	public void bookHotelTest() throws Exception {
		HotelBookingInfoAndStatus hotelInfo=new HotelBookingInfoAndStatus();
		
		TourPackageAndStatus tourPackage=new TourPackageAndStatus();
		tourPackage.setTripBookingId(1);
		tourPackage.setNoOfhotelBeds(1);
		Mockito.when(hotelService.bookHotel(Mockito.anyInt(),Mockito.anyInt())).thenReturn(hotelInfo);
		ResultActions response=mockMvc.perform(post("/hotels/book-a-hotel")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(tourPackage)));
		response.andExpect(status().isOk());
		
		
	}
	

}
