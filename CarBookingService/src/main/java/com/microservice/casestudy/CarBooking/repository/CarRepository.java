package com.microservice.casestudy.CarBooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.casestudy.CarBooking.entity.CarInfo;


@Repository
public interface CarRepository extends JpaRepository<CarInfo,Integer> {
}


