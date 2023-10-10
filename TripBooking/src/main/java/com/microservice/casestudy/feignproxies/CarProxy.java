package com.microservice.casestudy.feignproxies;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.microservice.casestudy.entity.CarInfo;



@FeignClient(name="car-service",url="localhost:8083")
public interface CarProxy {
	
	@GetMapping("/cars/get-car-info")
	public List<CarInfo> getCarInfo();

}
