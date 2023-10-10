package com.microservice.casestudy.HotelBooking.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.microservice.casestudy.HotelBooking.exception.EmptyInputException;

@ControllerAdvice
public class HotelControllerAdvice {
	
	@ExceptionHandler(EmptyInputException.class)
	public ResponseEntity<String> handleEmptyInput(EmptyInputException emptyInputException){
		return new ResponseEntity<String>(emptyInputException.getErrorMessage(),HttpStatus.BAD_REQUEST);
	}
	

}
