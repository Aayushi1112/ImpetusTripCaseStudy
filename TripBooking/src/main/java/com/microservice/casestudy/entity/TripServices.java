package com.microservice.casestudy.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TripServices {
	private List<HotelInfo> hotels;
	private List<FlightInfo> flights;
	private List<CarInfo> cars;
	

}
