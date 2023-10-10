package com.microservice.casestudy.feignproxies;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.microservice.casestudy.entity.FlightInfo;



@FeignClient(name="flight-service",url="localhost:8082")
public interface FlightProxy {
	
	@GetMapping("/flights/get-flight-info")
	public List<FlightInfo> getFlightInfo();

}
