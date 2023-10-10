package com.microservice.casestudy.feignproxies;

import java.util.List;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.microservice.casestudy.entity.HotelInfo;



@FeignClient(name="hotel-service",url="localhost:8081")
public interface HotelProxy {
	
	@GetMapping("/hotels/get-hotel-info")
	public List<HotelInfo> getHotelInfo();

}
