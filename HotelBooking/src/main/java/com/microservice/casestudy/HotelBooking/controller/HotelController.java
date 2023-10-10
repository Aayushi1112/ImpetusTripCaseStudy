package com.microservice.casestudy.HotelBooking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.casestudy.HotelBooking.entity.HotelBookingInfoAndStatus;
import com.microservice.casestudy.HotelBooking.entity.HotelInfo;
import com.microservice.casestudy.HotelBooking.entity.TourPackageAndStatus;
import com.microservice.casestudy.HotelBooking.exception.EmptyInputException;
import com.microservice.casestudy.HotelBooking.service.HotelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/hotels")
@Tag(name = "Hotel-Service", description = "Hotel APIs")
public class HotelController {
	
	@Autowired
	HotelService hotelService;
	
	
		
	 @ApiResponses({
		    @ApiResponse(responseCode = "200", description="found",content = { @Content(schema = @Schema(implementation = HotelInfo.class), mediaType = "application/json") }),
		    @ApiResponse(responseCode = "404", description = "HOtel welcome not found", content = { @Content(schema = @Schema()) })
		  })
	@GetMapping("/welcome")
	 @Operation(
		      summary = "Retrieve a HOtel name",
		      description = "Get a HOtel welcome message.",
		      tags = { "HotelINfo", "get" })
	public String welcome() {
		return "Welcome to the hotel service";
	}
	
	@GetMapping("/welcometoexception")
	public String welcomeException() {
		throw new EmptyInputException("400","Sorry");
	}
	
	@GetMapping("/get-hotel-info")
	//@ApiOperation(value="Gives hotel info",notes="returns api notes",response=HotelInfo.class)
	
	public  ResponseEntity<List<HotelInfo>> getHotelInfo() {
		List<HotelInfo> hotels=hotelService.getHotelInfo();
		return ResponseEntity.ok(hotels);
			//	ResponseEntity.status(null);
	}
	
	 @ApiResponses({
		    @ApiResponse(responseCode = "200", description="Added",content = { @Content(schema = @Schema(implementation = HotelInfo.class), mediaType = "application/json") }),
		    @ApiResponse(responseCode = "404", description = "Hotel not added", content = { @Content(schema = @Schema()) })
		  })
	 @Parameters({
		    @Parameter(name = "hotelName", description = "HOtel name "),
		    @Parameter(name = "hotelperBedPrice", description = "Per bed price of hotel", required = true),
		    @Parameter(name = "noOfBedsAvailableInHotel", description = "Number of beds currently available", required = true)
		  })
	 @Operation(
		      summary = "Add hotel information",
		      description = "Adding hotel name,hotel bed per price and number of beds in hotel",
		      tags = { "HotelServcie", "post" })
	@PostMapping("/add-hotel-info")
	public ResponseEntity<HotelInfo> addHotelInfo(@RequestBody HotelInfo hotelInfo) {
		return ResponseEntity.ok(hotelService.addHotelInfo(hotelInfo));
	}
	
	@PostMapping("/book-a-hotel")
	public ResponseEntity<HotelBookingInfoAndStatus> bookHotel(@RequestBody TourPackageAndStatus tourPackage) throws JsonProcessingException {
	int tripBookingId=tourPackage.getTripBookingId();
	int noOfHotelBedsNeeded=tourPackage.getNoOfhotelBeds();
	return ResponseEntity.ok(hotelService.bookHotel(tripBookingId, noOfHotelBedsNeeded));
	
	
	}
}
